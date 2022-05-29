package wook.pool.board.base

import androidx.lifecycle.MutableLiveData

fun MutableLiveData<Int>.plus(value: Int) {
    this.postValue(this.value!! + value)
}

fun MutableLiveData<Int>.minus(value: Int) {
    if (this.value!! <= 0) return
    this.postValue(this.value!! - value)
}