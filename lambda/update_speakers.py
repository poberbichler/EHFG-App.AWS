import json
import html
import os

import re

import boto3
from bs4 import BeautifulSoup

import requests

YEAR = os.environ.get("YEAR", "2016")
s3 = boto3.client('s3')


def lambda_handler(event, context):
    print("Received event: " + json.dumps(event, indent=2))
    cleaned_input = _clean_input(requests.get(f"http://www.ehfg.org/feed/speakers/data/{YEAR}.rss").text)
    s3.put_object(Bucket="ehfg-app", Key="speakers.json", Body=cleaned_input)


def _clean_input(html_input):
    soup = BeautifulSoup(html.unescape(html_input.replace("&nbsp;", "")), "html.parser")

    _remove_color(soup.channel.find_all('font'))
    _remove_font_family(soup.channel.select('[style*=font-family]'))
    _remove_empty_tags(soup.channel.find_all(["a", "p"]))

    return soup.prettify()


def _remove_font_family(tags):
    regex = re.compile(r"font-family:.*[A-Za-z0-9]*;?")
    for tag in tags:
        tag["style"] = regex.sub("", tag["style"])


def _remove_empty_tags(tags):
    [tag.extract() for tag in tags if not tag.text.strip()]


def _remove_color(fonts):
    for font in fonts:
        del font['color']
        font["style"] = "font-style: italic"
