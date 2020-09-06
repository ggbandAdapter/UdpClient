# UdpClient

# Jsclient
Android and JS interactive tools.

[![](https://jitpack.io/v/ggbandAdapter/UdpClient.svg)](https://jitpack.io/#ggbandAdapter/UdpClient)
[![Platform](https://img.shields.io/badge/platform-android-green.svg)](http://developer.android.com/index.html)
![SDK](https://img.shields.io/badge/SDK-15%2B-green.svg)
--

## How to introduce dependency

To get a Git project into your build:

Step 1. Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}

Step 2. Add the dependency

	dependencies {
	        implementation 'com.github.ggbandAdapter:UdpClient:v1.0.1'
	}


--

## How to use

First, create a client that interacts with Udp.
``` java
  val acUdpClient = UdpClient.Builder()
        .ip(Constants.DEVICE_AC_IP)
        .sPort(Constants.DEVICE_AC_PORT)
        .rPort(Constants.RECEIVED_AC_PORT)
        .convert(AcUdpResConvertImpl()).build()

val acUdpApi = acUdpClient.create(AcUdpApi::class.java)
```
   
### Android send Udp package.

Step 1. Writing java interface Sending Upd method
   
 ``` java
 interface AcUdpApi {
    fun scan(@Field body: ScanPackage): Call<ScanMessage>
    
    fun add(@Field body: ScanPackage): Call<String>
}
```

Step 3. Call Upd Api.

 ``` java
    acUdpApi.scan(ScanPackage()).send({ data, inde ->
               
            }, {

            })
```
--- For more details, please see [example](https://github.com/ggbandAdapter/UdpClient/tree/master/app)

