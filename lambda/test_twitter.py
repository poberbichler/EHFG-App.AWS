import importlib
import json
import os
from io import BytesIO
from unittest.mock import MagicMock

import boto3
import pytest
from botocore.response import StreamingBody
from botocore.stub import Stubber


@pytest.fixture
def tweets():
    with open("test/tweets.json") as f:
        return json.load(f)


@pytest.fixture
def twitter(mocker, tweets):
    s3 = boto3.client('s3')
    stubber = Stubber(s3)
    streaming_body = StreamingBody(BytesIO(bytes(json.dumps(tweets), 'utf-8')), len(json.dumps(tweets)))
    expected_params = {"Bucket": "ehfg-app123555", "Key": "twitter.json"}
    stubber.add_response("get_object", {"Body": streaming_body}, expected_params)
    stubber.activate()
    mocker.patch.object(boto3, 'client', MagicMock(return_value=s3))

    os.environ["PAGE_SIZE"] = "3"
    os.environ["HASHTAG"] = "#EHFG2016"

    import twitter
    importlib.reload(twitter)
    return twitter


def test_tweets_by_id(twitter):
    response = twitter.lambda_handler({"tweetId": "830841883505012736"}, "context")
    assert len(response) == 5


def test_tweets_by_page_id(twitter):
    result = twitter.lambda_handler({"pageId": "0"}, "context")

    assert len(result["data"]) == 3
    assert result["currentPage"] == 0
    assert result["morePages"] == True


def test_tweets_by_timestamp(twitter):
    newer_tweets = twitter.lambda_handler({"timestamp": "1486851487999"}, "context")
    assert len(newer_tweets) == 10
