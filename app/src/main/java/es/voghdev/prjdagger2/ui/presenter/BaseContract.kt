package es.voghdev.prjdagger2.ui.presenter

class BaseContract {
    interface Presenter<in T> {
        fun initialize()
        fun resume()
        fun pause()
        fun destroy()
        fun attach(view: T)
    }

    interface View {
    }
}