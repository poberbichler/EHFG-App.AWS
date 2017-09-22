from unittest.mock import MagicMock

import boto3
import pytest
import requests
from botocore.stub import Stubber


@pytest.fixture
def handler(mocker):
    s3 = boto3.client('s3')
    stubber = Stubber(s3)

    with open('expected_sessions.json') as f:
        stubber.add_response("put_object", {}, expected_params={
            'Body': f.read().encode("UTF-8"),
            "Bucket": "ehfg-app",
            "Key": "sessions.json"
        })

    stubber.activate()
    mocker.patch.object(boto3, 'client', MagicMock(return_value=s3))

    class MockRequest:
        def __init__(self, file_name):
            with open(file_name) as f:
                self.text = f.read()

    mocker.patch.object(requests, 'get', MagicMock(side_effect=[MockRequest('sessions.xml'), MockRequest("speaker_events.xml")]))

    import update_sessions
    return update_sessions


def test_update_speakers(handler):
    result = handler.lambda_handler({}, "context")

    assert len(result) == 2

    w1 = result[0]
    assert w1.id == "1001"
    assert w1.name == "Investing in healthier cities: \"insuring\" prevention"
    assert w1.description == "<p>The health insurance sector is engaged in the politics of pooling health risks while city mayors are in the politics of " \
                             "managing cities. Most of the focus of the health insurance sector is around reducing the costs of treatment rather than " \
                             "prevention for better health and well-being with a subsequent reduced need for treatment. " \
                             "This session proposes to introduce promotive and preventive health into the politics of health insurance and thus encourage " \
                             "healthier cities. It will cover how city mayors can work together with health insurance agencies and companies, " \
                             "guided by health experts, to provide a platform where members of the public and particularly clients of the insurance " \
                             "companies receive information on promoting healthy lifestyle choices and prevention and early treatment of NCDs. " \
                             "The modalities of how this can be initiated and implemented will be discussed and the potential and approaches for " \
                             "improving health literacy for NCDs also explored. Mayors will be encouraged to champion this initiative even as it expands " \
                             "beyond cities.</p><p>The full list of speakers will be announced shortly. </p>"
    assert w1.location == "Kursaal C"
    assert w1.code == "W1"
    assert w1.start_time == "09:00"
    assert w1.end_time == "11:00"
    assert w1.speakers == []

    w2 = result[1]
    assert w2.speakers == ['1177', '925', '1180', '924', '1178', '1256', '672']
