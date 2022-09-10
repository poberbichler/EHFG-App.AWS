from unittest.mock import MagicMock

import boto3
import pytest
import requests
from botocore.stub import Stubber


@pytest.fixture
def handler(mocker):
    s3 = boto3.client('s3')
    stubber = Stubber(s3)

    with open('expected_speakers.json') as f:
        stubber.add_response("put_object", {}, expected_params={
            'Body': f.read().encode("UTF-8"),
            "Bucket": "ehfg-app",
            "Key": "speakers.json"
        })

    stubber.activate()
    mocker.patch.object(boto3, 'client', MagicMock(return_value=s3))

    class MockRequest:
        def __init__(self, file_name):
            with open(file_name, encoding="UTF-8") as f:
                self.text = f.read()

    mocker.patch.object(requests, 'get', MagicMock(return_value=MockRequest('speakers.xml')))

    import update_speakers
    return update_speakers


def test_update_speakers(handler):
    result = handler.lambda_handler({}, 'context')

    speaker = result[0]
    json_speaker = speaker.json()

    assert speaker.id == json_speaker["id"]
    assert speaker.first_name == json_speaker["firstName"]
    assert speaker.last_name == json_speaker["lastName"]
    assert speaker.full_name == json_speaker["fullName"]
    assert speaker.image_url == json_speaker["imageUrl"]

    assert result[0].image_url == "https://www.ehfg.org/fileadmin/_processed_/8/f/csm_Dyakova_Mariana_dadb5102a0.jpg"
    assert result[1].image_url == "http://www.ehfg.org/intranet/uploads/speakersdefaultperson.jpg"
