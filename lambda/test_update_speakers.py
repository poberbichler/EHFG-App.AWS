import update_speakers


def test_update_speakers():
    with open('test/speakers.xml', 'r') as f:
        html_input = f.read()

    print(update_speakers._clean_input(html_input))
