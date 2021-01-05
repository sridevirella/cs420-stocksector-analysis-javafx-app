# Recession Impact Analysis on Stock Market Sectors

The main objective is to examine how the 11 stock market sectors (Materials, Industrials, Financials, Energy, Consumer discretionary, Information technology, Communication services, Real estate, Health care, Consumer staples and Utilities ) are affected by two major recessions in 2008 and 2020. This application analyzes which sectors majorly rose or fell during both recessions, which sectors were able maintain a steady growth despite recession, and how long it has taken for majorly affected sectors to recover from losses.

By examining the retrieved dataset through my application, I answered the following questions:

- What is the most affected stock market sector during the 2008 and 2020 recessions? 
- How long has it taken for the most affected sector to recover from its low after the 2008 recession? 
- Display stock trends from 2008 to 2020 for a user selected sector.

## Dataset:

I used TIME_SERIES_MONTHLY API from the website https://www.alphavantage.co/ that has data related to all equity stocks from the 1990 to 2020. As a response, this API returns historical data of a specified global equity on a monthly basis in JSON format. This JSON object contains monthly trading details of equity stock such as last trading day of each month, monthly open, monthly high, monthly low, monthly close and monthly volume. For this project, I retrieved a part of data set for 11 sectors from the 2008 to 2020. 

## Running Application:

Installation: at least Java 11, https://www.oracle.com/index.html.
Clone the repository and open/import project to a Java IDE. Import dependencies using gradle. Run JavaFx application from main -> java -> SectorDataApp.java.
