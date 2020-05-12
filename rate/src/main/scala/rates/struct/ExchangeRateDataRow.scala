package rates.struct

case class ExchangeRateDataRow(
                                currencyCode: String,
                                crossCurrencyCode: String,
                                crossCurrencyRate: Double
                              )