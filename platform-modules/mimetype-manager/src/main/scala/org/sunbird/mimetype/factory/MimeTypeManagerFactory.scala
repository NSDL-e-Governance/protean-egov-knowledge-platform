package org.sunbird.mimetype.factory

import org.apache.commons.lang3.StringUtils
import org.sunbird.cloudstore.StorageService
import org.sunbird.common.Platform
import org.sunbird.mimetype.mgr.MimeTypeManager
import org.sunbird.mimetype.mgr.impl.{ApkMimeTypeMgrImpl, AssetMimeTypeMgrImpl, CollectionMimeTypeMgrImpl, DefaultMimeTypeMgrImpl, DocumentMimeTypeMgrImpl, EcmlMimeTypeMgrImpl, H5PMimeTypeMgrImpl, HtmlMimeTypeMgrImpl, PluginMimeTypeMgrImpl, YouTubeMimeTypeMgrImpl}

object MimeTypeManagerFactory {

	implicit val ss: StorageService = new StorageService
	val ONLINE_MIMETYPES: java.util.List[String] = Platform.getStringList("content.mimeType.online", java.util.Arrays.asList("video/youtube", "video/x-youtube", "text/x-url"))

	val defaultMimeTypeMgrImpl = new DefaultMimeTypeMgrImpl
	val mimeTypeMgr = Map[String, MimeTypeManager](
		"video/youtube" -> new YouTubeMimeTypeMgrImpl,"video/x-youtube" -> new YouTubeMimeTypeMgrImpl,
		"text/x-url" -> new YouTubeMimeTypeMgrImpl,
		"application/pdf" -> new DocumentMimeTypeMgrImpl, "application/epub" -> new DocumentMimeTypeMgrImpl,
		"application/msword" -> new DocumentMimeTypeMgrImpl,
	    "assets" -> new AssetMimeTypeMgrImpl,
		"application/vnd.ekstep.ecml-archive" -> new EcmlMimeTypeMgrImpl,
		"application/vnd.ekstep.html-archive" -> new HtmlMimeTypeMgrImpl,
		"application/vnd.ekstep.content-collection" -> new CollectionMimeTypeMgrImpl,
		"application/vnd.ekstep.plugin-archive" -> new PluginMimeTypeMgrImpl,
		"application/vnd.ekstep.h5p-archive" -> new H5PMimeTypeMgrImpl,
		"application/vnd.android.package-archive" -> new ApkMimeTypeMgrImpl
	)

	def getManager(objectType: String, mimeType: String): MimeTypeManager = {
		if(ONLINE_MIMETYPES.contains(mimeType))
			mimeTypeMgr.getOrElse(mimeType.toLowerCase(), defaultMimeTypeMgrImpl)
		else if (StringUtils.equalsIgnoreCase("Asset", objectType)) {
			mimeTypeMgr.get("assets").get
		} else {
			if(null != mimeType)
				mimeTypeMgr.getOrElse(mimeType.toLowerCase(), defaultMimeTypeMgrImpl)
			else defaultMimeTypeMgrImpl
		}
	}
}
