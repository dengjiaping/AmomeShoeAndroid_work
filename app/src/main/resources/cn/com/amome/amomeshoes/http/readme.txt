1、AsynHttpDowanloadFile 下载类。下载文件或者图片。
2、ClientConstant 网络常量类，定义接口地址和calltype。
3、HttpError 网络返回错误类型类。
4、HttpService http服务。定义了一个接口，用于返回http请求或失败。包含的参数有接口请求的type，statusCode，http头信息，以及json。
5、PostAsyncTask 获取网络数据类，post请求。通过接口请求数据时，首先调用其中的startAsyncTask(Context mCnt, HttpService.ICallback mCb, final int type, RequestParams params, String url)方法，在该方法中创建AsyncHttpClient实例用于向服务端请求数据。
1、如果成功，判断是否statusCode=200，不是则返回服务器繁忙。如果是，判断服务端返回的json数据中return_code。如果return_code=1,判断是证书错误，如果是拿到新的证书加密后重新请求，如果不是证书错误，返回相应的错误。最后调用ICallback接口的onHttpPostSuccess方法在activity处理服务端返回的数据。
2、如果失败，判断statusCode的错误类型给出提示，最后调用ICallback的onHttpPostFailure方法。