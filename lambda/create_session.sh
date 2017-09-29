#!/bin/bash

FOLDER_NAME="session-assembly"
SCRIPT_NAME="update_session.zip"

echo "installing dependencies to folder '${FOLDER_NAME}'"
pip install requests -t ${FOLDER_NAME} > /dev/null
pip install bs4 -t ${FOLDER_NAME} > /dev/null
cp update_sessions.py ${FOLDER_NAME}

echo "zipping result file '${SCRIPT_NAME}'"
cd ${FOLDER_NAME}
zip -r ../${SCRIPT_NAME} * > /dev/null
cd ..

echo "removing folder '${FOLDER_NAME}'"
rm -rf ${FOLDER_NAME}
