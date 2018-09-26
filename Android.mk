LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

LOCAL_JAVA_LIBRARIES := bouncycastle conscrypt telephony-common
LOCAL_STATIC_JAVA_LIBRARIES := android-support-v4 android-support-v13 jsr305
ifdef DOLBY_DAP
LOCAL_JAVA_LIBRARIES += framework_ext
else
LOCAL_STATIC_JAVA_LIBRARIES += libsds
endif #DOLBY_DAP

LOCAL_MODULE_TAGS := optional

LOCAL_SRC_FILES := \
        $(call all-java-files-under, src) \
        src/com/android/settings/EventLogTags.logtags

LOCAL_PACKAGE_NAME := FHSettings
ALL_DEFAULT_INSTALLED_MODULES += $(LOCAL_PACKAGE_NAME)
LOCAL_CERTIFICATE := platform
LOCAL_PRIVILEGED_MODULE := true

#LOCAL_STATIC_JAVA_LIBRARIES += NetShare
#LOCAL_STATIC_JAVA_LIBRARIES += HiAoService
#LOCAL_STATIC_JAVA_LIBRARIES += DisplaySetting
#LOCAL_STATIC_JAVA_LIBRARIES += SDKInvoke
#LOCAL_STATIC_JAVA_LIBRARIES += HiDLNA
#LOCAL_STATIC_JAVA_LIBRARIES += libHiMultiScreen
LOCAL_PROGUARD_FLAG_FILES := proguard.flags
LOCAL_PROGUARD_ENABLED := disabled

LOCAL_AAPT_FLAGS += -c zz_ZZ

include $(BUILD_PACKAGE)

ifndef DOLBY_DAP
include $(CLEAR_VARS)
#LOCAL_PREBUILT_STATIC_JAVA_LIBRARIES := libsds:ds.jar
include $(BUILD_MULTI_PREBUILT)
endif

# Use the folloing include to make our test apk.
include $(call all-makefiles-under,$(LOCAL_PATH))
