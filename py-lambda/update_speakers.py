import json

import boto3
import requests
from bs4 import BeautifulSoup

print('Loading function')
s3 = boto3.client('s3')


def lambda_handler(event, context):
    print(f"event: [{event}], context: [{context}]")

    event_soup = BeautifulSoup(requests.get("https://www.ehfg.org/xml-interface/speaker-events/").text, "html.parser")
    event_speakers = set([id.text for id in event_soup.find_all('speakerid')])

    soup = BeautifulSoup(requests.get("https://www.ehfg.org/xml-interface/speakers/").text, "html.parser")
    speakers = [Speaker(speaker) for speaker in soup.channel.find_all('item') if speaker.id.text in event_speakers]

    filtered = [speaker.fullname.text for speaker in soup.find_all('item') if speaker.id.text not in event_speakers]
    print(f"filtered speakers - {len(filtered)}")
    print(f"valid speakers    - {len(speakers)}")

    speakers.sort(key=lambda speaker: speaker.full_name)

    result = json.dumps([speaker.json() for speaker in speakers], indent=2)

    s3.put_object(Bucket="ehfg-app", Key="speakers.json",
                  Body=(result.encode("utf-8")))

    return result


class Speaker:
    def __init__(self, speaker):
        self.id = speaker.id.text
        self.first_name = speaker.firstname.text
        self.last_name = speaker.lastname.text
        self.description = speaker.find("bio:encoded").text.strip()
        self.image_url = speaker.imgpath.text or "http://www.ehfg.org/intranet/uploads/speakersdefaultperson.jpg"
        self.full_name = speaker.fullname.text

    def json(self):
        return {"id": self.id,
                "firstName": self.first_name,
                "lastName": self.last_name,
                "description": self.description,
                "imageUrl": self.image_url,
                "fullName": self.full_name
                }


if __name__ == "__main__":
    lambda_handler("event", "context")
