import json
import os
from io import BytesIO
from unittest.mock import MagicMock

import boto3
import pytest
from pytest_mock import mocker
from botocore.response import StreamingBody
from botocore.stub import Stubber

@pytest.fixture
def tweets():
    with open("test/tweets.json") as f:
        return json.load(f)


os.environ["PAGE_SIZE"] = "3"
os.environ["HASHTAG"] = "#EHFG2016"

import twitter


def test_tweets_by_id(tweets, mocker):
    s3 = boto3.client('s3')
    with Stubber(s3) as stubber:
        streaming_body = StreamingBody(BytesIO(bytes(json.dumps(tweets), 'utf-8')), len(json.dumps(tweets)))
        expected_params = {"Bucket": "ehfg-app", "Key": "twitter.json"}
        stubber.add_response("get_object", {"Body": streaming_body}, expected_params)
        mocker.patch.object(boto3, 'client', MagicMock(return_value=s3))
        response = twitter.lambda_handler({"tweetId": "830841883505012736"}, "context")

    assert len(response) == 5


def test_tweets_by_page_id(tweets):
    result = twitter.TweetPage(tweets, 0).json()
    print(result)

    assert len(result["data"]) == 3
    assert result["currentPage"] == 0
    assert result["morePages"] == True


def test_tweets_by_timestamp(tweets):
    newer_tweets = twitter._find_newer_by_id(tweets, "830841883505012736")
    assert len(newer_tweets) == 5

