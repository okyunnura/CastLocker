#CastLocker

##Build
gradlew bootRun

##Params
```
-Daws.bucketName=${bucket name}
-Daws.accessKeyId=${access key}
-Daws.secretKey=${secret key}
```

##AWS
IAM used policy
```
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Allow",
            "Action": "s3:*",
            "Resource": "*"
        },
        {
            "Effect": "Allow",
            "Action": "sts:*",
            "Resource": "*"
        }
    ]
}
```
