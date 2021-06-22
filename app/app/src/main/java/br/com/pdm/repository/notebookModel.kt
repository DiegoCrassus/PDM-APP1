package br.com.pdm

class NotebookModel(title: String?, text: String?, id_notebook: Int?, email: String?, name: String?) {
    private var title: String = "Sem titulo"
    private var text: String = ""
    private var id_notebook: Int = 0
    private var email: String = ""
    private var name: String = ""

    init {
        this.title = title!!
        this.text = text!!
        this.id_notebook = id_notebook!!
        this.email = email!!
        this.name = name!!
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
    fun getId_notebook(): Int? {
        return id_notebook
    }
    fun setId_notebook(id_notebook: Int?) {
        this.id_notebook = id_notebook!!
    }
    fun getEmail(): String {
        return email
    }
    fun setEmail(email: String?) {
        this.email = email!!
    }
    fun getName(): String {
        return name
    }
    fun setName(name: String?) {
        this.name = name!!
    }

}