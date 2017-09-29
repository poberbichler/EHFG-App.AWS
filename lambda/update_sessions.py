import calendar
import itertools
import json
import time

import boto3
import requests
from bs4 import BeautifulSoup

print('Loading function')
s3 = boto3.client('s3')

DAY_MAP = {"Day 1": "2017-10-04",
           "Day 2": "2017-10-05",
           "Day 3": "2017-10-06"
           }


def lambda_handler(event, context):
    print(f"event: [{event}], context: [{context}]")

    sessions = BeautifulSoup(requests.get("https://www.ehfg.org/xml-interface/events/").text, "html.parser")
    speaker_event_text = requests.get("https://www.ehfg.org/xml-interface/speaker-events/").text
    speaker_event_text = speaker_event_text.replace("</id>", "</eventId>")
    speakers = BeautifulSoup(speaker_event_text, "html.parser")

    speaker_events = {}
    for event, speakers in itertools.groupby(speakers.find_all("item"), key=lambda x: x.eventid):
        speaker_events[event.text] = [item.speakerid.text for item in speakers]

    sessions_with_speakers = [Session(session, speaker_events) for session in sessions.find_all("item")]

    result = {}
    for day, date in DAY_MAP.items():
        result[date] = {
            "description": day,
            "sessions": [session.json() for session in sessions_with_speakers if session.day == day]
        }

    result_json = json.dumps(result, indent=2)
    s3.put_object(Bucket="ehfg-app", Key="sessions.json", Body=(result_json.encode("utf-8")))
    return result_json


class Session:
    @staticmethod
    def convert(session, timestamp):
        parsed_time = time.strptime(f"{DAY_MAP[session.day.text]} {timestamp.text}", "%Y-%m-%d %H:%M")
        return int(round(calendar.timegm(parsed_time) * 1000))

    def __init__(self, session, speakers):
        self.id = session.id.text
        self.name = session.event.text
        self.description = session.find("description:encoded").text
        self.start_time = Session.convert(session, session.start)
        self.end_time = Session.convert(session, session.end)
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


if __name__ == "__main__":
    lambda_handler("event", "context")
