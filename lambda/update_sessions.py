import itertools
import json

import boto3
import requests
from bs4 import BeautifulSoup

print('Loading function')
s3 = boto3.client('s3')


def lambda_handler(event, context):
    print(f"event: [{event}], context: [{context}]")

    sessions = BeautifulSoup(requests.get("https://www.ehfg.org/xml-interface/events/").text, "html.parser")
    speaker_event_text = requests.get("https://www.ehfg.org/xml-interface/speaker-events/").text
    speaker_event_text = speaker_event_text.replace("</id>", "</eventId>")
    speakers = BeautifulSoup(speaker_event_text, "html.parser")

    speaker_events = {}
    for event, speakers in itertools.groupby(speakers.find_all("item"), key=lambda x: x.eventid):
        speaker_events[event.text] = [item.speakerid.text for item in speakers]

    result = [Session(session, speaker_events) for session in sessions.find_all("item")]
    s3.put_object(Bucket="ehfg-app", Key="sessions.json", Body=(json.dumps([session.json() for session in result], indent=2).encode("utf-8")))
    return result


class Session:
    def __init__(self, session, speakers):
        self.id = session.id.text
        self.name = session.event.text
        self.description = session.find("description:encoded").text
        self.start_time = ""
        self.end_time = ""
        self.code = session.code.text
        self.day = session.day.text
        self.location = session.room.text
        self.speakers = speakers.get(self.id, [])

    def json(self):
        return {
            "id": self.id,
            "name": self.name,
            "description": self.description,
            "startTime": self.start_time,
            "endTime": self.end_time,
            "location": self.location,
            "speakers": self.speakers,
            "code": self.code
        }
