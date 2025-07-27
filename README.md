# Stocks App

**Stocks App** is a modern Android application that helps users stay updated with the stock market. Users can view top gainers and losers, explore detailed company overviews, and manage personalized watchlists.
The app allows saving stocks to watchlists, which are accessible even offline, providing a smooth and reliable experience for market enthusiasts.
---

## Features

## Home Screen
Users can view Top Gainers and Top Losers with limited items on the home screen.

A "View All" button allows users to see the complete list of gainers or losers, based on their selection.

 ## Stock Details
Tapping on any Top Gainer or Top Loser opens a detailed Company Overview screen showing relevant financial and stock information with line graph.

 ## Watchlist Management
Users can save or unsave stocks (gainers or losers) to a Watchlist.

Watchlists can be created, modified by the user.

Supports adding stocks to existing watchlists or creating a new watchlist on the fly.

 ## Offline Access
Watchlists are persisted locally, allowing users to access their saved stocks offline.

---

##  Screenshots
<table>

   <tr>
    <td>
      <img src="https://github.com/AyushPorwal10/README_SCREENSHOTS/blob/main/home_screen.png" alt="Home Screen" width="200"/>
      <h2>Home Screen</h2>
    </td>
    <td>
      <img src="https://github.com/AyushPorwal10/README_SCREENSHOTS/blob/main/top_20_gainers.png" alt="Top 20 Gainers" width="200"/>
      <h2>Top 20 Gainers</h2>
    </td>
    <td>
      <img src="https://github.com/AyushPorwal10/README_SCREENSHOTS/blob/main/top_20_losers.png" alt="Top 20 Losers" width="200"/>
      <h2>Top 20 Losers</h2>
    </td>
    <td>
      <img src="https://github.com/AyushPorwal10/README_SCREENSHOTS/blob/main/company_overview_.png" alt="Company Overview" width="200"/>
      <h2>Company Overview</h2>
    </td>
  </tr>
  
  <tr>
    <td>
      <img src="https://github.com/AyushPorwal10/README_SCREENSHOTS/blob/main/company_overview.png" alt="Company Overview" width="200"/>
      <h2>Company Overview</h2>
    </td>
    <td>
      <img src="https://github.com/AyushPorwal10/README_SCREENSHOTS/blob/main/add_item_to_new_or_existing_watchlist.png" alt="Manage Watchlists" width="200"/>
      <h2>Manage Watchlists</h2>
    </td>
    <td>
      <img src="https://github.com/AyushPorwal10/README_SCREENSHOTS/blob/main/view_created_watchlist.png" alt="Created Watchlists" width="200"/>
      <h2>Created Watchlists</h2>
    </td>
    <td>
      <img src="https://github.com/AyushPorwal10/README_SCREENSHOTS/blob/main/search.png" alt="Search Stocks" width="200"/>
      <h2>Search Stocks</h2>
    </td>
  </tr>

  <tr>
    <td>
      <img src="https://github.com/AyushPorwal10/README_SCREENSHOTS/blob/main/watchlist_items.png" alt="Watchlist Items" width="200"/>
      <h2>Watchlist Items</h2>
    </td>
    
  </tr>
  
</table>

---

## Tech Stack

This project is built using the following technologies:

- **Kotlin** – Primary language for Android development  
- **Hilt (Dagger-Hilt)** – For dependency injection  
- **Retrofit** – For handling network/API requests  
- **Jetpack Compose** – Modern declarative UI toolkit for Android  
- **Kotlin Coroutines & Flow** – For asynchronous and reactive programming  
- **Jetpack ViewModel & StateFlow** – For lifecycle-aware state management   
- **MVVM Architecture** – Separates concerns across Model, View, and ViewModel for better code organization and lifecycle-aware data handling.

---

**Download Apk File**
Drive Link - https://drive.google.com/file/d/1mTSvNwEBXWJzzsya0lv_hnJZBXt5ihg5/view?usp=drive_link


  ## API Key & Base URL Configuration

##  Cloning & Setup

1. **Clone the repository:**
   ```bash
   git clone https://github.com/AyushPorwal10/Groww_Assignment
  

**Note:**  
If you're cloning or downloading this repository to run the project locally, you **must provide your own API key and base URL**.

Create a file named `secret.properties` in the **root directory** of the project with the following structure:

   
```properties
ALPHA_VANTAGE_API_KEY=your_api_key_here
ALPHA_VANTAGE_BASE_URL=https://www.alphavantage.co
