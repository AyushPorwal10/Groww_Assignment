package com.example.growwassignment.gainerloser.checkdata

import com.example.growwassignment.gainerloser.marketdatamodels.CompanyOverviewData

 object CheckCompanyOverviewData {
     fun isCompanyOverviewEmpty(data: CompanyOverviewData): Boolean {
        return data.Symbol.isNullOrBlank() &&
                data.Name.isNullOrBlank() &&
                data.MarketCapitalization.isNullOrBlank()
    }
}