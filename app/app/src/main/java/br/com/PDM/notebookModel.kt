package br.com.PDM

class NotebookModel(title: String?, text: String?) {
    private var title: String = "Sem titulo"
    private var text: String = ""
    init {
        this.title = title!!
        this.text = text!!
    }
    fun getTitle(): String? {
        return title
    }
    fun setTitle(title: String?) {
        this.title = title!!
    }
    fun getText(): String? {
        return text
    }
    fun setText(text: String?) {
        this.text = text!!
    }
}