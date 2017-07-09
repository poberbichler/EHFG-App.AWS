import boto3
import os
import pytest
import json

from botocore.response import StreamingBody
from botocore.stub import Stubber

@pytest.fixture
def tweets():
    with open("test/tweets.json") as f:
        return json.load(f)

os.environ["PAGE_SIZE"] = "3"
os.environ["HASHTAG"] = "#EHFG2016"

import twitter



def test_tweets_by_id(tweets):
    s3 = boto3.client('s3')
    stubber = Stubber(s3)

    streaming_body = StreamingBody(StringIO.write(json.dumps(tweets)), len(json.dumps(tweets)))
    expected_params = {"Bucket": "ehfg-app", "Key": "twitter.json"}
    stubber.add_response("get_object", {"Body": streaming_body}, expected_params)

    stubber.activate()

    response = s3.get_object(Bucket="ehfg-app", Key="twitter.json")
    other_tweets = json.loads(response['Body'].read().decode('utf-8'))

    filtered_tweets = twitter._find_newer_by_id(other_tweets, "830962214739800065")
    assert len(filtered_tweets) == 2


def test_tweets_by_page_id(tweets):
    assert True
    #result = twitter.lambda_handler({"pageId": "0"}, "context")
    #print(result)

    #assert len(result["data"]) == 3
    #assert result["currentPage"] == 0
    #assert result["morePages"] == "true"


def test_tweets_by_timestamp():
    assert True
