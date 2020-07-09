package org.wordpress.mobile.WPAndroidGlue

import android.os.Bundle
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GutenbergProps(
    val mentionsFlag: Boolean,
    val isSiteUsingWpComRestApi: Boolean,
    val isUnsupportedBlockEditorEnabled: Boolean,
    val localeSlug: String,
    val postType: String,
    val editorTheme: Bundle?,
    var translations: Bundle? = null,
    var isDarkMode: Boolean? = null,
    var htmlModeEnabled: Boolean? = null
) : Parcelable {

    constructor(
        mentionsFlag: Boolean,
        isSiteUsingWpComRestApi: Boolean,
        isUnsupportedBlockEditorEnabled: Boolean,
        localeSlug: String,
        postType: String,
        editorTheme: Bundle?
    ) : this(
            mentionsFlag = mentionsFlag,
            isSiteUsingWpComRestApi = isSiteUsingWpComRestApi,
            isUnsupportedBlockEditorEnabled = isUnsupportedBlockEditorEnabled,
            localeSlug = localeSlug,
            postType = postType,
            editorTheme = editorTheme,
            translations = null,
            isDarkMode = null,
            htmlModeEnabled = null
    )

    private fun getCapabilities() = Bundle().apply {
        putBoolean(
                PROP_NAME_CAPABILITIES_MENTIONS,
                mentionsFlag && isSiteUsingWpComRestApi
        )
        putBoolean(
                PROP_NAME_CAPABILITIES_UNSUPPORTED_BLOCK_EDITOR,
                isUnsupportedBlockEditorEnabled
        )
    }

    fun getInitialProps(bundle: Bundle?) = (bundle ?: Bundle()).apply {
        putString(PROP_NAME_INITIAL_DATA, "")
        putString(PROP_NAME_INITIAL_TITLE, "")
        putString(PROP_NAME_LOCALE, localeSlug)
        putString(PROP_NAME_POST_TYPE, postType)
        putBundle(PROP_NAME_TRANSLATIONS, requireNotNull(translations))
        putBoolean(PROP_NAME_INITIAL_HTML_MODE_ENABLED, requireNotNull(htmlModeEnabled))
        putBundle(PROP_NAME_CAPABILITIES, getCapabilities())

        editorTheme?.getSerializable(PROP_NAME_COLORS)?.let { colors ->
            putSerializable(PROP_NAME_COLORS, colors)
        }

        editorTheme?.getSerializable(PROP_NAME_GRADIENTS)?.let { gradients ->
            putSerializable(PROP_NAME_GRADIENTS, gradients)
        }
    }

    companion object {

        fun initContent(bundle: Bundle?, title: String?, content: String?) =
                (bundle ?: Bundle()).apply {
                    title?.let { putString(PROP_NAME_INITIAL_TITLE, it) }
                    content?.let { putString(PROP_NAME_INITIAL_DATA, it) }
                }

        private const val PROP_NAME_INITIAL_DATA = "initialData"
        private const val PROP_NAME_INITIAL_TITLE = "initialTitle"
        private const val PROP_NAME_INITIAL_HTML_MODE_ENABLED = "initialHtmlModeEnabled"
        private const val PROP_NAME_POST_TYPE = "postType"
        private const val PROP_NAME_LOCALE = "locale"
        private const val PROP_NAME_TRANSLATIONS = "translations"
        private const val PROP_NAME_CAPABILITIES = "capabilities"
        private const val PROP_NAME_CAPABILITIES_MENTIONS = "mentions"
        private const val PROP_NAME_CAPABILITIES_UNSUPPORTED_BLOCK_EDITOR = "unsupportedBlockEditor"
        private const val PROP_NAME_COLORS = "colors"
        private const val PROP_NAME_GRADIENTS = "gradients"
    }
}
