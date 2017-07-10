import json
import os
from itertools import takewhile

import boto3

print('Loading function')

PAGE_SIZE = int(os.environ['PAGE_SIZE'])
CURRENT_HASHTAG = os.environ['HASHTAG']
s3 = boto3.client('s3')


def lambda_handler(event, context):
    print("Received event: " + json.dumps(event, indent=2))

    try:
        response = s3.get_object(Bucket="ehfg-app", Key="twitter.json")
        tweets = json.loads(response['Body'].read().decode('utf-8'))

        if "pageId" in event:
            return TweetPage(tweets, int(event["pageId"])).json()
        elif "timestamp" in event:
            return [tweet for tweet in tweets if tweet["timestamp"] > int(event["timestamp"])]
        elif "tweetId" in event:
            return _find_newer_by_id(tweets, event["tweetId"])
        elif "tweet" in event:
            return _add_tweet(s3, tweets, event["tweet"])
        else:
            raise ValueError("only 'pageId' and 'timestamp' are allowed as input params")
    except Exception as e:
        print(e)
        raise e


class TweetPage:
    def __init__(self, all_tweets, page_id):
        self.data = all_tweets[page_id * 0: (page_id + 1) * PAGE_SIZE]
        self.max_pages = len(all_tweets) / PAGE_SIZE
        self.current_page = page_id
        self.more_pages = page_id != self.max_pages
        self.current_hashtag = CURRENT_HASHTAG

    def json(self):
        return {"data": self.data,
                "maxPages": self.max_pages,
                "currentPage": self.current_page,
                "morePages": self.more_pages,
                "currentHashtag": self.current_hashtag
                }


def _find_newer_by_id(all_tweets, tweetId):
    result = []
    for tweet in takewhile(lambda t: t["id"] != tweetId, all_tweets):
        result.append(tweet)

    return result


def _add_tweet(s3, all_tweets, new_tweet):
    all_tweets.insert(0, new_tweet)
    s3.put_object(Bucket="ehfg-app", Key="twitter.json", Body=json.dumps(all_tweets, indent=2).encode("utf-8"))
    return all_tweets
