package com.image

import android.app.Application
import com.facebook.cache.disk.DiskCacheConfig
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory
import com.facebook.imagepipeline.cache.MemoryCacheParams
import com.facebook.imagepipeline.core.ImagePipelineConfig
import com.image.di.appModule
import okhttp3.OkHttpClient
import org.koin.android.ext.android.inject
import org.koin.android.ext.android.startKoin

/**
 * 기본 설정 Application
 */
class ImageApplication : Application() {

    private val okHttpClient: OkHttpClient by inject()
    private val maxHeapSize = Runtime.getRuntime().maxMemory().toInt()
    private val maxMemoryCacheSize = maxHeapSize / 4

    override fun onCreate() {
        super.onCreate()
        startKoin(this, appModule)

        val config: ImagePipelineConfig = OkHttpImagePipelineConfigFactory
            .newBuilder(this, okHttpClient)
            .setBitmapMemoryCacheParamsSupplier {
                MemoryCacheParams(
                    maxMemoryCacheSize,
                    Int.MAX_VALUE,
                    maxMemoryCacheSize,
                    Int.MAX_VALUE,
                    Int.MAX_VALUE
                )
            }
            .setMainDiskCacheConfig(
                DiskCacheConfig.newBuilder(this)
                    .setBaseDirectoryPath(cacheDir)
                    .setBaseDirectoryName(getString(R.string.disk_cache_dir_name))
                    .build()
            )
            .setDownsampleEnabled(true)
            .build()
        Fresco.initialize(this, config)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        Fresco.getImagePipeline().clearMemoryCaches()
    }
}