aws lambda create-function \
 --function-name ehfg-session-update-kt \
 --role arn:aws:iam::187475763678:role/service-role/ehfg-session-update-kt-role-emwuc8g9 \
 --zip-file fileb://target/lambda-0.0.1-SNAPSHOT-aws.jar \
 --handler org.springframework.cloud.function.adapter.aws.FunctionInvoker::handleRequest \
 --description "Kotlin testing" \
 --runtime java11 \
 --region eu-central-1 \
 --timeout 15 \
 --memory-size 512 \
 --publish
