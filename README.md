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

S3 CORS
```
<?xml version="1.0" encoding="UTF-8"?>
<CORSConfiguration xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
	<CORSRule>
		<AllowedOrigin>*</AllowedOrigin>
		<AllowedMethod>GET</AllowedMethod>
		<AllowedMethod>PUT</AllowedMethod>
		<AllowedMethod>POST</AllowedMethod>
		<AllowedMethod>DELETE</AllowedMethod>
		<MaxAgeSeconds>3600</MaxAgeSeconds>
		<ExposeHeader>ETag</ExposeHeader>
		<ExposeHeader>x-amz-request-id</ExposeHeader>
		<ExposeHeader>x-amz-id-2</ExposeHeader>
		<ExposeHeader>x-amz-meta-custom-header</ExposeHeader>
		<AllowedHeader>Authorization</AllowedHeader>
		<AllowedHeader>Access-Control-Allow-Origin</AllowedHeader>
		<AllowedHeader>*</AllowedHeader>
	</CORSRule>
</CORSConfiguration>
```