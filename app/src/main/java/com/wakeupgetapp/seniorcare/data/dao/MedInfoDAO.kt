package com.wakeupgetapp.seniorcare.data.dao

data class MedInfoDAO (
    var firstName: String = "",
    var lastName: String = "",
    var birthday: String = "",
    var illnesses: String = "",
    var bloodType: String = "",
    var allergies: String = "",
    var medication: String = "",
    var height: String = "",
    var weight: String = "",
    var languages: String = "",
    var others: String = "",
        ) {
    override fun toString(): String {
        return  "Imię: $firstName\n" +
                "Nazwisko: $lastName\n" +
                "Data urodzenia: $birthday\n" +
                "Choroby: $illnesses\n" +
                "Grupa krwi: $bloodType\n" +
                "Alergie: $allergies\n" +
                "Przyjmowane leki: $medication\n" +
                "Wzrost: $height\n" +
                "Waga: $weight\n" +
                "Główny język: $languages\n" +
                "Inne: $others\n"
    }
}