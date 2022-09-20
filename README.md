# EHFG-App.AWS
Playing around with an AWS based serverless backend for the EHFG App

# How to deploy lambda
```
cd kt-lambda
mvn clean package
private-aws-profile
./update
```

# How to deploy web app
```
cd mobile
ionic build --prod
private-aws-profile
aws s3 sync www s3:ehfg-app-public
```

# How to build and deploy for iOS
```
cd mobile

```

# mobile knowledge
### Create splashscreen and icons

https://capacitorjs.com/docs/guides/splash-screens-and-icons

```
# In case the plugin is not installed
# npm install -g cordova-res

cordova-res ios --skip-config --copy
cordova-res android --skip-config --copy
```