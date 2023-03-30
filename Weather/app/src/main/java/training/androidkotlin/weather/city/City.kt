package training.androidkotlin.weather.city

data class City (
    var id: Long,
    val name: String){
    constructor(name: String) : this (-1, name)
}