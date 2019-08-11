package es.voghdev.prjdagger2.ui.picasso

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory

import com.squareup.picasso.Cache

import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.UnsupportedEncodingException

class PicassoImageCache(private val mContext: Context, private val mCacheDir: File) : Cache {
	
	override fun get(key: String): Bitmap? {
		var key = key
		key = String.format("%s.png", transform(key))
		val f = File(mCacheDir, key)
		return getBitmapFromFile(f.absolutePath)
	}
	
	override fun set(key: String, bitmap: Bitmap) {
		var key = key
		key = String.format("%s.png", transform(key))
		val f = File(mCacheDir, key)
		saveBitmapToFile(bitmap, f.absolutePath)
	}
	
	override fun size(): Int {
		val children = mCacheDir.list()
		return children?.size ?: 0
	}
	
	override fun maxSize(): Int {
		return 1000
	}
	
	override fun clear() {
		val children = mCacheDir.list()
		for(i in children.indices) {
			File(mCacheDir, children[i]).delete()
		}
	}
	
	private fun transform(s: String): String {
		return s.replace("[^A-Za-z0-9]".toRegex(), "").substring(15)
	}
	
	//region Util methods (could be moved to another Util class)
	fun saveBitmapToFile(bmp: Bitmap, absolutePath: String) {
		bitmapToFile(getBytesFromBitmap(bmp), absolutePath)
	}
	
	private fun bitmapToFile(data: ByteArray, absolutePath: String?) {
		if(absolutePath == null) {
			return
		}
		val fos: FileOutputStream
		try {
			fos = FileOutputStream(absolutePath)
			fos.write(data, 0, data.size)
			fos.close()
		} catch(e: FileNotFoundException) {
			e.printStackTrace()
		} catch(e: UnsupportedEncodingException) {
			e.printStackTrace()
		} catch(e: IOException) {
			e.printStackTrace()
		}
		
	}
	
	private fun getBytesFromBitmap(bmp: Bitmap): ByteArray {
		val bos = ByteArrayOutputStream()
		bmp.compress(Bitmap.CompressFormat.PNG, 100, bos)
		return bos.toByteArray()
	}
	
	override fun clearKeyUri(keyPrefix: String) {
		/* Empty */
	}
	
	companion object {
		
		fun getBitmapFromFile(absolutePath: String): Bitmap? {
			val file = File(absolutePath)
			return if(file.exists()) {
				BitmapFactory.decodeFile(absolutePath)
			} else null
		}
	}
	
	//endregion
}