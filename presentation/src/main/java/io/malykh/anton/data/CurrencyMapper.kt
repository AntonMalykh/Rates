package io.malykh.anton.data

import android.content.res.Resources
import android.graphics.drawable.Drawable
import io.malykh.anton.base.DataMapper
import io.malykh.anton.core.data.entity.Currency
import io.malykh.anton.core.data.entity.Currency.*
import io.malykh.anton.presentation.R

internal class CurrencyMapper(private val resources: Resources): DataMapper<Currency, CurrencyExt> {

    override fun map(from: Currency): CurrencyExt =
        CurrencyExt(from, from.getLocalizedDescription(), from.getFlag())

    private fun Currency.getLocalizedDescription(): String {
        val descriptionResId: Int = when (this) {
            EUR -> R.string.currency_euro
            AUD -> R.string.currency_australian_dollar
            BGN -> R.string.currency_bulgarian_lev
            BRL -> R.string.currency_brazilian_real
            CAD -> R.string.currency_canadian_dollar
            CHF -> R.string.currency_swiss_franc
            CNY -> R.string.currency_yuan_renminbi
            CZK -> R.string.currency_czech_koruna
            DKK -> R.string.currency_danish_krone
            GBP -> R.string.currency_pound_sterling
            HKD -> R.string.currency_hong_kong_dollar
            HRK -> R.string.currency_kuna
            HUF -> R.string.currency_forint
            IDR -> R.string.currency_rupiah
            ILS -> R.string.currency_lilangeni
            INR -> R.string.currency_indian_rupee
            ISK -> R.string.currency_iceland_krona
            JPY -> R.string.currency_yen
            KRW -> R.string.currency_south_korean_won
            MXN -> R.string.currency_mexican_peso
            MYR -> R.string.currency_malaysian_ringgit
            NOK -> R.string.currency_norwegian_krone
            NZD -> R.string.currency_new_zealand_dollar
            PHP -> R.string.currency_philippine_peso
            PLN -> R.string.currency_zloty
            RON -> R.string.currency_romanian_leu
            RUB -> R.string.currency_russian_ruble
            SEK -> R.string.currency_swedish_krona
            SGD -> R.string.currency_singapore_dollar
            THB -> R.string.currency_baht
            TRY -> R.string.currency_turkish_lira
            USD -> R.string.currency_us_dollar
            ZAR -> R.string.currency_rand
        }
        return resources.getString(descriptionResId)
    }

    private fun Currency.getFlag(): Drawable {
        val flagResId: Int = when (this) {
            EUR -> R.drawable.currency_euro
            AUD -> R.drawable.currency_australian_dollar
            BGN -> R.drawable.currency_bulgarian_lev
            BRL -> R.drawable.currency_brazilian_real
            CAD -> R.drawable.currency_canadian_dollar
            CHF -> R.drawable.currency_swiss_franc
            CNY -> R.drawable.currency_yuan_renminbi
            CZK -> R.drawable.currency_czech_koruna
            DKK -> R.drawable.currency_danish_krone
            GBP -> R.drawable.currency_pound_sterling
            HKD -> R.drawable.currency_hong_kong_dollar
            HRK -> R.drawable.currency_kuna
            HUF -> R.drawable.currency_forint
            IDR -> R.drawable.currency_rupiah
            ILS -> R.drawable.currency_lilangeni
            INR -> R.drawable.currency_indian_rupee
            ISK -> R.drawable.currency_iceland_krona
            JPY -> R.drawable.currency_yen
            KRW -> R.drawable.currency_south_korean_won
            MXN -> R.drawable.currency_mexican_peso
            MYR -> R.drawable.currency_malaysian_ringgit
            NOK -> R.drawable.currency_norwegian_krone
            NZD -> R.drawable.currency_new_zealand_dollar
            PHP -> R.drawable.currency_philippine_peso
            PLN -> R.drawable.currency_zloty
            RON -> R.drawable.currency_romanian_leu
            RUB -> R.drawable.currency_russian_ruble
            SEK -> R.drawable.currency_swedish_krona
            SGD -> R.drawable.currency_singapore_dollar
            THB -> R.drawable.currency_baht
            TRY -> R.drawable.currency_turkish_lira
            USD -> R.drawable.currency_us_dollar
            ZAR -> R.drawable.currency_rand
        }
        return resources.getDrawable(flagResId, null)
    }
}