package io.malykh.anton.data

import android.content.res.Resources
import android.graphics.drawable.Drawable
import io.malykh.anton.base.DataMapper
import io.malykh.anton.core.data.entity.Currency
import io.malykh.anton.core.data.entity.Currency.*
import io.malykh.anton.core.data.entity.CurrencyRate
import io.malykh.anton.presentation.R

internal class CurrencyMapper(private val resources: Resources): DataMapper<CurrencyRate, CurrencyExt> {

    override fun map(from: CurrencyRate): CurrencyExt =
        CurrencyExt(from, from.currency.getLocalizedDescription(), from.currency.getFlag())

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
            EUR -> R.drawable.ic_currency_euro
            AUD -> R.drawable.ic_currency_australian_dollar
            BGN -> R.drawable.ic_currency_bulgarian_lev
            BRL -> R.drawable.ic_currency_brazilian_real
            CAD -> R.drawable.ic_currency_canadian_dollar
            CHF -> R.drawable.ic_currency_swiss_franc
            CNY -> R.drawable.ic_currency_yuan_renminbi
            CZK -> R.drawable.ic_currency_czech_koruna
            DKK -> R.drawable.ic_currency_danish_krone
            GBP -> R.drawable.ic_currency_pound_sterling
            HKD -> R.drawable.ic_currency_hong_kong_dollar
            HRK -> R.drawable.ic_currency_kuna
            HUF -> R.drawable.ic_currency_forint
            IDR -> R.drawable.ic_currency_rupiah
            ILS -> R.drawable.ic_currency_lilangeni
            INR -> R.drawable.ic_currency_indian_rupee
            ISK -> R.drawable.ic_currency_iceland_krona
            JPY -> R.drawable.ic_currency_yen
            KRW -> R.drawable.ic_currency_south_korean_won
            MXN -> R.drawable.ic_currency_mexican_peso
            MYR -> R.drawable.ic_currency_malaysian_ringgit
            NOK -> R.drawable.ic_currency_norwegian_krone
            NZD -> R.drawable.ic_currency_new_zealand_dollar
            PHP -> R.drawable.ic_currency_philippine_peso
            PLN -> R.drawable.ic_currency_zloty
            RON -> R.drawable.ic_currency_romanian_leu
            RUB -> R.drawable.ic_currency_russian_ruble
            SEK -> R.drawable.ic_currency_swedish_krona
            SGD -> R.drawable.ic_currency_singapore_dollar
            THB -> R.drawable.ic_currency_baht
            TRY -> R.drawable.ic_currency_turkish_lira
            USD -> R.drawable.ic_currency_us_dollar
            ZAR -> R.drawable.ic_currency_rand
        }
        return resources.getDrawable(flagResId, null)
    }
}