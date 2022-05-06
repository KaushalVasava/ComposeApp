package com.lahsuak.apps.mycompose

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.bluetooth.BluetoothClass
import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.provider.Settings.Secure
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.util.DisplayMetrics
import android.util.Log
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.math.BigInteger
import java.net.InetAddress
import java.net.NetworkInterface
import java.security.MessageDigest
import java.util.*
import kotlin.experimental.and


object Util {
    fun getDeviceInfo(activity: Context, device: Constants): String {
        try {
            when (device) {
                Constants.DEVICE_LANGUAGE -> return Locale.getDefault().displayLanguage
                Constants.DEVICE_TIME_ZONE -> return TimeZone.getDefault()
                    .id //(false, TimeZone.SHORT);
                Constants.DEVICE_LOCAL_COUNTRY_CODE -> return activity.resources
                    .configuration.locale.country
                Constants.DEVICE_CURRENT_YEAR -> return "" + Calendar.getInstance()
                    .get(Calendar.YEAR)
                Constants.DEVICE_CURRENT_DATE_TIME -> {
                    val calendarTime: Calendar =
                        Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault())
                    val time: Long = calendarTime.timeInMillis / 1000
                    return time.toString()
                }
                Constants.DEVICE_CURRENT_DATE_TIME_ZERO_GMT -> {
                    val calendarTime_zero: Calendar =
                        Calendar.getInstance(TimeZone.getTimeZone("GMT+0"), Locale.getDefault())
                    return java.lang.String.valueOf(calendarTime_zero.timeInMillis / 1000)
                }
                Constants.DEVICE_HARDWARE_MODEL -> return deviceName
                Constants.DEVICE_NUMBER_OF_PROCESSORS -> return Runtime.getRuntime()
                    .availableProcessors()
                    .toString() + ""
                Constants.DEVICE_LOCALE -> return Locale.getDefault().isO3Country
//                Constants.DEVICE_IP_ADDRESS_IPV4 -> return getIPAddress(true)
//                Constants.DEVICE_IP_ADDRESS_IPV6 -> return getIPAddress(false)
                Constants.DEVICE_MAC_ADDRESS -> {
                    var mac = getMACAddress("wlan0")
                    if (TextUtils.isEmpty(mac)) {
                        mac = getMACAddress("eth0")
                    }
                    if (TextUtils.isEmpty(mac)) {
                        mac = "DU:MM:YA:DD:RE:SS"
                    }
                    return mac
                }
                Constants.DEVICE_TOTAL_MEMORY -> {
                    return getTotalMemory(activity).toString()
                }
                Constants.DEVICE_FREE_MEMORY -> return getFreeMemory(activity).toString()
                Constants.DEVICE_USED_MEMORY -> {
                    val freeMem = getTotalMemory(activity) - getFreeMemory(activity)
                    return freeMem.toString()
                    return ""
                }
                Constants.DEVICE_TOTAL_CPU_USAGE -> {
                    val cpu = cpuUsageStatistic
                    if (cpu != null) {
                        val total = cpu[0] + cpu[1] + cpu[2] + cpu[3]
                        return total.toString()
                    }
                    return ""
                }
                Constants.DEVICE_TOTAL_CPU_USAGE_SYSTEM -> {
                    val cpu_sys = cpuUsageStatistic
                    if (cpu_sys != null) {
                        val total = cpu_sys[1]
                        return total.toString()
                    }
                    return ""
                }
                Constants.DEVICE_TOTAL_CPU_USAGE_USER -> {
                    val cpu_usage = cpuUsageStatistic
                    if (cpu_usage != null) {
                        val total = cpu_usage[0]
                        return total.toString()
                    }
                    return ""
                }
                Constants.DEVICE_MANUFACTURE -> return Build.MANUFACTURER
                Constants.DEVICE_SYSTEM_VERSION -> return deviceName
                Constants.DEVICE_VERSION -> return Build.VERSION.SDK_INT.toString()
                Constants.DEVICE_IN_INCH -> return getDeviceInch(activity)
                Constants.DEVICE_TOTAL_CPU_IDLE -> {
                    val cpu_idle = cpuUsageStatistic
                    if (cpu_idle != null) {
                        val total = cpu_idle[2]
                        return total.toString()
                    }
                    return ""
                }
//                Constants.DEVICE_NETWORK_TYPE -> return getNetworkType(activity)
//                Constants.DEVICE_NETWORK -> return checkNetworkStatus(activity)
//                Constants.DEVICE_TYPE -> return if (isTablet(activity)) {
//                    if (getDeviceMoreThan5Inch(activity)) {
//                        "Tablet"
//                    } else "Mobile"
//                } else {
//                    "Mobile"
//                }
                Constants.DEVICE_SYSTEM_NAME -> return "Android OS"
                else -> {}
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    fun getDeviceId(context: Context): String? {
        var device_uuid = Secure.getString(context.contentResolver, Secure.ANDROID_ID)
        if (device_uuid == null) {
            device_uuid = "12356789" // for emulator testing
        } else {
            try {
                var _data = device_uuid.toByteArray()
                val _digest: MessageDigest = MessageDigest.getInstance("MD5")
                _digest.update(_data)
                _data = _digest.digest()
                val _bi: BigInteger = BigInteger(_data).abs()
                device_uuid = _bi.toString(36)
            } catch (e: Exception) {
                if (e != null) {
                    e.printStackTrace()
                }
            }
        }
        return device_uuid
    }

    @SuppressLint("NewApi")
    private fun getTotalMemory(activity: Context): Long {
        return try {
            val mi = ActivityManager.MemoryInfo()
            val activityManager =
                activity.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            activityManager.getMemoryInfo(mi)
            mi.totalMem / 1048576L
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }

    private fun getFreeMemory(activity: Context): Long {
        return try {
            val mi = ActivityManager.MemoryInfo()
            val activityManager =
                activity.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            activityManager.getMemoryInfo(mi)
            mi.availMem / 1048576L
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }

    private val deviceName: String
        get() {
            val manufacturer = Build.MANUFACTURER
            val model = Build.MODEL
            return if (model.startsWith(manufacturer)) {
                capitalize(model)
            } else {
                capitalize(manufacturer) + " " + model
            }
        }

    private fun capitalize(s: String?): String {
        if (s == null || s.length == 0) {
            return ""
        }
        val first = s[0]
        return if (Character.isUpperCase(first)) {
            s
        } else {
            Character.toUpperCase(first).toString() + s.substring(1)
        }
    }

    /**
     * Convert byte array to hex string
     *
     * @param bytes
     * @return
     */
    private fun bytesToHex(bytes: ByteArray): String {
        val sbuf = StringBuilder()
        for (idx in bytes.indices) {
            val intVal: Byte = bytes[idx] and 0xff.toByte()
            if (intVal < 0x10) sbuf.append("0")
            sbuf.append(Integer.toHexString(intVal.toInt()).uppercase())
        }
        return sbuf.toString()
    }

    /**
     * Returns MAC address of the given interface name.
     *
     * @param interfaceName eth0, wlan0 or NULL=use first interface
     * @return mac address or empty string
     */
    @SuppressLint("NewApi")
    private fun getMACAddress(interfaceName: String?): String {
        try {
            val interfaces: List<NetworkInterface> =
                Collections.list(NetworkInterface.getNetworkInterfaces())
            for (intf in interfaces) {
                if (interfaceName != null) {
                    if (!intf.getName().equals(interfaceName,true)) continue
                }
                val mac: ByteArray = intf.getHardwareAddress() ?: return ""
                val buf = StringBuilder()
                for (idx in mac.indices) buf.append(String.format("%02X:", mac[idx]))
                if (buf.length > 0) buf.deleteCharAt(buf.length - 1)
                return buf.toString()
            }
        } catch (ex: Exception) {
            return ""
        } // for now eat exceptions
        return ""
        /*
             * try { // this is so Linux hack return
             * loadFileAsString("/sys/class/net/" +interfaceName +
             * "/address").toUpperCase().trim(); } catch (IOException ex) { return
             * null; }
             */
    }

    /**
     * Get IP address from first non-localhost interface
     *
     * @return address or empty string
     */
//    private fun getIPAddress(useIPv4: Boolean): String {
//        try {
//            val interfaces: List<NetworkInterface> =
//                Collections.list(NetworkInterface.getNetworkInterfaces())
//            for (intf in interfaces) {
//                val addrs: List<InetAddress> = Collections.list(intf.getInetAddresses())
//                for (addr in addrs) {
//                    if (!addr.isLoopbackAddress()) {
//                        val sAddr: String = addr.getHostAddress().toUpperCase()
//                        val isIPv4: Boolean = InetAddressUtils.isIPv4Address(sAddr)
//                        if (useIPv4) {
//                            if (isIPv4) return sAddr
//                        } else {
//                            if (!isIPv4) {
//                                val delim = sAddr.indexOf('%') // drop ip6 port
//                                // suffix
//                                return if (delim < 0) sAddr else sAddr.substring(0, delim)
//                            }
//                        }
//                    }
//                }
//            }
//        } catch (ex: Exception) {
//        } // for now eat exceptions
//        return ""
//    }

    /*
            *
            * @return integer Array with 4 elements: user, system, idle and other cpu
            * usage in percentage.
            */
    private val cpuUsageStatistic: IntArray?
        get() = try {
            var tempString = executeTop()
            tempString = tempString!!.replace(",".toRegex(), "")
            tempString = tempString.replace("User".toRegex(), "")
            tempString = tempString.replace("System".toRegex(), "")
            tempString = tempString.replace("IOW".toRegex(), "")
            tempString = tempString.replace("IRQ".toRegex(), "")
            tempString = tempString.replace("%".toRegex(), "")
            for (i in 0..9) {
                tempString = tempString!!.replace("  ".toRegex(), " ")
            }
            tempString = tempString!!.trim { it <= ' ' }
            val myString = tempString.split(" ".toRegex()).toTypedArray()
            val cpuUsageAsInt = IntArray(myString.size)
            for (i in myString.indices) {
                myString[i] = myString[i].trim { it <= ' ' }
                cpuUsageAsInt[i] = myString[i].toInt()
            }
            cpuUsageAsInt
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("executeTop", "error in getting cpu statics")
            null
        }

    private fun executeTop(): String? {
        var p: Process? = null
        var `in`: BufferedReader? = null
        var returnString: String? = null
        try {
            p = Runtime.getRuntime().exec("top -n 1")
            `in` = BufferedReader(InputStreamReader(p.inputStream))
            while (returnString == null || returnString.contentEquals("")) {
                returnString = `in`.readLine()
            }
        } catch (e: IOException) {
            Log.e("executeTop", "error in getting first line of top")
            e.printStackTrace()
        } finally {
            try {
                `in`?.close()
                p!!.destroy()
            } catch (e: IOException) {
                Log.e("executeTop", "error in closing and destroying top process")
                e.printStackTrace()
            }
        }
        return returnString
    }

//    fun getNetworkType(activity: Context): String {
//        var networkStatus = ""
//        val connMgr = activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        // check for wifi
//        val wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
//        // check for mobile data
//        val mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
//        networkStatus = if (wifi!!.isAvailable) {
//            "Wifi"
//        } else if (mobile!!.isAvailable) {
//            getDataType(activity)
//        } else {
//            "noNetwork"
//        }
//        return networkStatus
//    }

//    fun checkNetworkStatus(activity: Context): String {
//        var networkStatus = ""
//        try {
//            // Get connect mangaer
//            val connMgr =
//                activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//            // // check for wifi
//            val wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
//            // // check for mobile data
//            val mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
//            if (wifi!!.isAvailable) {
//                networkStatus = "Wifi"
//            } else if (mobile!!.isAvailable) {
//                //networkStatus = getDataType(activity)
//            } else {
//                networkStatus = "noNetwork"
//                networkStatus = "0"
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//            networkStatus = "0"
//        }
//        return networkStatus
//    }

//    fun isTablet(context: Context): Boolean {
//        return context.getResources()
//            .getConfiguration().screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK >= Configuration.SCREENLAYOUT_SIZE_LARGE
//    }

    fun getDeviceMoreThan5Inch(activity: Context): Boolean {
        return try {
            val displayMetrics: DisplayMetrics = activity.getResources().getDisplayMetrics()
            // int width = displayMetrics.widthPixels;
            // int height = displayMetrics.heightPixels;
            val yInches = displayMetrics.heightPixels / displayMetrics.ydpi
            val xInches = displayMetrics.widthPixels / displayMetrics.xdpi
            val diagonalInches = Math.sqrt((xInches * xInches + yInches * yInches).toDouble())
            if (diagonalInches >= 7) {
                // 5inch device or bigger
                true
            } else {
                // smaller device
                false
            }
        } catch (e: Exception) {
            false
        }
    }

    fun getDeviceInch(activity: Context): String {
        return try {
            val displayMetrics: DisplayMetrics = activity.getResources().getDisplayMetrics()
            val yInches = displayMetrics.heightPixels / displayMetrics.ydpi
            val xInches = displayMetrics.widthPixels / displayMetrics.xdpi
            val diagonalInches = Math.sqrt((xInches * xInches + yInches * yInches).toDouble())
            diagonalInches.toString()
        } catch (e: Exception) {
            "-1"
        }
    }

//    fun getDataType(activity: Context): String {
//        var type = "Mobile Data"
//        val tm = activity.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
//        when (tm.networkType) {
//            TelephonyManager.NETWORK_TYPE_HSDPA -> {
//                type = "Mobile Data 3G"
//                Log.d("Type", "3g")
//            }
//            TelephonyManager.NETWORK_TYPE_HSPAP -> {
//                type = "Mobile Data 4G"
//                Log.d("Type", "4g")
//            }
//            TelephonyManager.NETWORK_TYPE_GPRS -> {
//                type = "Mobile Data GPRS"
//                Log.d("Type", "GPRS")
//            }
//            TelephonyManager.NETWORK_TYPE_EDGE -> {
//                type = "Mobile Data EDGE 2G"
//                Log.d("Type", "EDGE 2g")
//            }
//        }
//        return type
//    }
}