package cn.ggband.udp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import cn.ggband.udp.ac.acUdpApi
import cn.ggband.udp.ac.bean.ScanPackage
import cn.ggband.udp.ac.send
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnScan.setOnClickListener {

            acUdpApi.scan(ScanPackage()).send({ data, inde ->
                Log.d("ggband","收到：scan data:${data};")
            }, {
                Log.d("ggband","收到：scan finish;")
            })

        }

        btnAdd.setOnClickListener {
            acUdpApi.add(ScanPackage().apply {
                cmd = 100
            }).send({ data, inde ->
                Log.d("ggband","收到：add data:${data};")

            }, {
                Log.d("ggband","收到：add fail;")
            })
        }


    }
}
