package cn.ggband.udp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cn.ggband.udp.ac.acUdpApi
import cn.ggband.udp.ac.bean.ScanPackage
import cn.ggband.udp.ac.send

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        acUdpApi.scan(ScanPackage()).send({ data, inde ->
        }, {

        })
    }
}
