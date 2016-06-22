# Android-Vinci-Image-Loader

Easy and fast way to load images on Android's app.

# Contact me?
* Email: hquspring@gmail.com
* Blog: http://blog.csdn.net/cj_star

# Compare with picasso and ImageLoader
![Screenshot](https://github.com/CJstar/ProjectResource/blob/master/all.png?raw=true)

# How to use?
Initialize config

```Java
//initialize the File Cache config
FileCacheOptions fileCacheOptions = new FileCacheOptions.Builder()
.setCacheRootPath(Environment.getExternalStorageDirectory().getPath() + "/VinciCache")
.setMaxCacheSize(20 * 1024 * 1024)
.setMaxFileCount(100)
.setIsUseFileCache(true)
.builder();
FileCache fileCache = BridgeFactory.createFileCache(Const.FileCacheType.LRUCACHE);
fileCache.setOptions(fileCacheOptions);

CustomExecutor executor = BridgeFactory.createExecutor(Const.ExecutorPoolType.FILOPOOL);
ExecutorOptions executorOptions = new ExecutorOptions.Builder().
setMaxExecutingSize(Runtime.getRuntime().availableProcessors() - 3)
.setMaxPoolSize(128)
.build();
executor.setOptions(executorOptions);

MemoryCacheOptions memoryCacheOptions = new MemoryCacheOptions.Builder()
.setmMaxCacheCount(100)
.setmMaxCacheSize(10 * 1024 * 1024)
.build();
MemmoryCache memmoryCache = BridgeFactory.createMemoryCache(Const.MemoryCacheType.LRUCACHE);
memmoryCache.setOptions(memoryCacheOptions);

VinciOptions vinciOptions = new VinciOptions.Builder()
.setmDefaultImageResId(R.mipmap.ic_launcher)
.setmErrorImageResId(R.mipmap.ic_launcher)
.setmFileCache(fileCache)
.setExecutor(executor)
.setmCornerSize(20)
.setmMemmoryCache(memmoryCache)
.build();
Vinci.getInstance().init(vinciOptions);
```

OR use default options
```Java
Vinci.getInstance().initByDefaultOptions();
```

And you can modify the load options like this:
```Java
Vinci.getInstance().getVinciOptions().setDefaultImageOptions(new ImageOptions.Builder()
                .setDefaultImageResId(R.mipmap.defaultimage)
                .build());
```

Load images
```Java
  Vinci.getInstance().showImage(imageView,url);
```

You can see the default options at cjstar.com.vincilibrary.core.consts.DefaultConfig.

# I want to tell you something.
* How about Vinci?
 Actually, Vinci include FileCache and MemoryCache, thread Excuter. All of them are customed, so you can study and learn or do what you want. You can improve this project in every where you can. We have no rulers.
 And now, we need you to join us to do something interesting at here.


* What the reason for me to start such a project?
You can see a lot of projects which are used to show image, such as ImageLoader or Picasso , Volley , and so on. This project is a stage for study, it has more space such as FileCache, MemoryCache, and thread Executer, you can do more and you can learn more.
In my opinion, The API is a guide for us to lean how to code, We can study from API and improve that.

