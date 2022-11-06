package wook.pool.board.global.base

import androidx.lifecycle.MutableLiveData
import wook.pool.board.Constant
import kotlin.math.absoluteValue

class ScoreLiveData(defaultValue: Int = 0) : MutableLiveData<Int>(defaultValue) {

    fun plus(value: Int = 1) {
        if (value < 0) {
            minus(value.absoluteValue)
            return
        }
        this.postValue(this.value!! + value)
    }

    fun minus(value: Int = 1) {
        if (this.value!! <= 0) return
        this.postValue(this.value!! - value)
    }

}