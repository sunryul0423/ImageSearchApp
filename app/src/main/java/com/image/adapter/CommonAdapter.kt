package com.image.adapter

import android.graphics.drawable.Animatable
import android.net.Uri
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.controller.BaseControllerListener
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.common.RotationOptions
import com.facebook.imagepipeline.image.ImageInfo
import com.facebook.imagepipeline.request.ImageRequestBuilder


/**
 * Presco 이미지 세팅
 * @param view xml의 SimpleDraweeView
 * @param url 이미지 Url
 */
@BindingAdapter("imgUrl")
fun setImageUrl(view: SimpleDraweeView, url: String?) {
    url?.let {
        val request = ImageRequestBuilder
            .newBuilderWithSource(Uri.parse(url))
            .setRotationOptions(RotationOptions.autoRotate()) // 화면 회전시 맞춤
            .setProgressiveRenderingEnabled(true) //개선된 JPEG이미지 스트리밍을 지원
            .build()

        // controller 생성 및 setting
        val draweeController = Fresco.newDraweeControllerBuilder().run {
            this.oldController = view.controller
            controllerListener = object : BaseControllerListener<ImageInfo>() {
                override fun onFinalImageSet(id: String?, imageInfo: ImageInfo?, animatable: Animatable?) {
                    super.onFinalImageSet(id, imageInfo, animatable)
                    imageInfo?.let {
                        view.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                        view.aspectRatio = imageInfo.width.toFloat() / imageInfo.height
                    }
                }
            }
            this.imageRequest = request
            this.build()
        }
        view.controller = draweeController
    }
}