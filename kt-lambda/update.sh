aws lambda update-function-code \
 --function-name ehfg-app-kt \
 --zip-file fileb://target/lambda-0.0.1-SNAPSHOT-aws.jar \
 --region eu-central-1 \
 --publish
