package com.tecsup.lab10tarea.data

import com.google.gson.annotations.SerializedName

data class ProductModel(
    @SerializedName("id")
    var id: Int,
    @SerializedName("name")
    var name: String,
    @SerializedName("price")
    var price: Double,
    @SerializedName("description")
    var description: String,
    @SerializedName("category")
    var category: String
)
