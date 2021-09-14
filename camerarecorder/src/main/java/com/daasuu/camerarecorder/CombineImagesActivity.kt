package com.daasuu.camerarecorder
import android.annotation.SuppressLint
import android.content.Intent
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.jaiselrahman.filepicker.model.MediaFile
import com.kalasa.library.R
import com.simform.videooperations.*
import com.timqi.sectorprogressview.SectorProgressView
import kotlinx.android.synthetic.main.activity_combine_images.*
import top.zibin.luban.Luban
import top.zibin.luban.OnCompressListener
import java.io.File
class CombineImagesActivity : BaseActivity(R.layout.activity_combine_images, R.string.merge_images) {
    private var isImageSelected: Boolean = false
    private val arr = ArrayList<Paths>()
    private val dimen_h = ArrayList<Int>()
    private val dimen_w = ArrayList<Int>()
    private var pics_size=0
    private var this_i=0
    private var VideoOutputPath="none"
    private var p_comp: TextView? = null
    private var spv: SectorProgressView? = null
    override fun initialization() {
        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }
        spv = findViewById<View>(R.id.spv) as SectorProgressView
        p_comp = findViewById<View>(R.id.progress_comp) as TextView
        Common.selectFile(this, maxSelection = 12, isImageSelection = true, isAudioSelection = false)
    }
    override fun onClick(v: View?) {
    }
    @SuppressLint("NewApi", "SetTextI18n")
    override fun selectedFiles(mediaFiles: List<MediaFile>?, requestCode: Int) {
        when (requestCode) {
            Common.IMAGE_FILE_REQUEST_CODE -> {
                if (mediaFiles != null && mediaFiles.isNotEmpty()) {
                    val size: Int = mediaFiles.size
                    pics_size= mediaFiles.size
                    for (i in mediaFiles) {
                        compressppic(i.path.toString())
                        dimen_h.add(i.height.toInt())
                        dimen_w.add(i.width.toInt())
                    }
                    isImageSelected = true
                } else {
                    Toast.makeText(this, getString(R.string.image_not_selected_toast_message), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun processStop() {
        runOnUiThread {
            mProgressView.visibility = View.GONE
        }
    }
    private fun compressppic(oldin_path: String?) {
        val oldFile = File(oldin_path)
        Luban.with(this)
            .load(oldFile)
            .setCompressListener(object : OnCompressListener {
                override fun onStart() {}
                override fun onSuccess(newFile: File) {
                    val paths = Paths()
                    paths.filePath = newFile.absolutePath
                    paths.isImageFile = true
                    arr.add(paths)
                    this_i=this_i+1
                    if(this_i==pics_size && isImageSelected){
                        processStart()
                        combineImagesProcess()
                    }
                }
                override fun onError(e: Throwable) {
                }
            }).launch()
    }
    private fun processStart() {
        mProgressView.visibility = View.VISIBLE
    }

    private fun combineImagesProcess() {
        val outputPath = Common.getFilePath(this, Common.VIDEO)
        val pathsList = ArrayList<Paths>()
        mediaFiles?.let {
            for (element in it) {
                val paths = Paths()
                paths.filePath = element.path
                paths.isImageFile = true
                pathsList.add(paths)
            }
            dimen_h.sort()
            dimen_w.sort()
            var h=dimen_h[dimen_h.size-1]
            var w=dimen_w[dimen_w.size-1]
            var thistime=pics_size*4
            if(h>2000)
            {
                h=1920
            }
            if(w>2000)
            {
                w=1080
            }
            val query = thiscombineImagesAndVideos(arr, w,h, "4", outputPath)
            CallBackOfQuery().callQuery(this, query, object : FFmpegCallBack {
                override fun statisticsProcess(statistics: Statistics) {
                    super.statisticsProcess(statistics)
                    var comp=((((statistics.time.toFloat()/1000)/(thistime.toFloat()/1000))*100).toInt()/1000)
                    spv?.setPercent((comp).toFloat())
                    p_comp?.text=((((statistics.time.toFloat()/1000)/(thistime.toFloat()/1000))*100).toInt()/1000).toString()+"%"
                }
                override fun process(logMessage: LogMessage) {
                }

                override fun success() {
                    processStop()
                    VideoOutputPath=outputPath;
                    finish()
                }

                override fun cancel() {
                    processStop()
                }

                override fun failed() {
                    processStop()
                }
            })
        }
    }
    override fun finish() {
        var time= pics_size*4;
        val data = Intent()
        data.putExtra("returnKey", VideoOutputPath.toString())
        data.putExtra("returntime",time)
        setResult(RESULT_OK, data)
        super.finish()
    }
    fun thiscombineImagesAndVideos(paths: ArrayList<Paths>, width: Int?, height: Int?, second: String, output: String): Array<String> {
        val inputs: ArrayList<String> = ArrayList()
        for (i in 0 until paths.size) {
            //for input
            if (paths[i].isImageFile) {
                inputs.add("-loop")
                inputs.add("1")
                inputs.add("-r")
                inputs.add("24")
                inputs.add("-t")
                inputs.add(second)
                inputs.add("-i")
                inputs.add(paths[i].filePath)
            } else {
                inputs.add("-i")
                inputs.add(paths[i].filePath)
            }
        }

        var query: String? = ""
        var queryAudio: String? = ""
        for (i in 0 until paths.size) {
            query = query?.trim()
            query += "[" + i + ":v]scale=${width}x${height},setdar=$width/$height[" + i + "v];"
            queryAudio = queryAudio?.trim()
            queryAudio += if (paths[i].isImageFile) {
                "[" + i + "v][" + paths.size + ":a]"
            } else {
                "[" + i + "v][" + i + ":a]"
            }
        }
        return getResult(inputs, query, queryAudio, paths, output)
    }

    private fun getResult(inputs: java.util.ArrayList<String>, query: String?, queryAudio: String?, paths: ArrayList<Paths>, output: String): Array<String> {
        inputs.apply {
            add("-f")
            add("lavfi")
            add("-t")
            add("0.1")
            add("-i")
            add("anullsrc")
            add("-filter_complex")
            add(query + queryAudio + "concat=n=" + paths.size + ":v=1:a=1 [v][a]")
            add("-map")
            add("[v]")
            add("-map")
            add("[a]")
            add("-preset")
            add("ultrafast")
            add(output)
        }
        return inputs.toArray(arrayOfNulls<String>(inputs.size))
    }
}