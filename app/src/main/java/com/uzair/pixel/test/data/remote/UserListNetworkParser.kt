package com.uzair.pixel.test.data.remote

import org.json.JSONObject

class UserListNetworkParser {
    fun parseJson(json: String): List<UserNetworkModel> {
        val list = mutableListOf<UserNetworkModel>()
        val rootObject = JSONObject(json)
        val jsonArray = rootObject.getJSONArray("items")

        for (i in 0 until jsonArray.length()) {
            val itemObject = jsonArray.getJSONObject(i)

            val id = itemObject.optInt("user_id", -1)
            val name = itemObject.optString("display_name")
            val imageUrl = itemObject.optString("profile_image")
            val reputation = itemObject.optInt("reputation", 0)

            if (id != -1 && name.isNotBlank()) {
                list.add(
                    UserNetworkModel(
                        id = id,
                        name = name,
                        imageUrl = imageUrl,
                        reputation = reputation
                    )
                )
            }
        }
        return list
    }
}