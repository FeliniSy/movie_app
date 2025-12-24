# Movie App - Android Java Application

A complete Android movie browsing application built with Java, following MVVM architecture pattern.

## Features

- **User Authentication**: Registration and login with SQLite-based local authentication
- **Movie Browsing**: Browse Popular, Top Rated, and Upcoming movies
- **Genre Filtering**: Filter movies by genre
- **Movie Search**: Real-time search functionality
- **Movie Details**: View detailed information about movies
- **Rating System**: Rate movies (1-5 stars) with local storage
- **Material Design**: Modern UI with Material Design components

## Architecture

The app follows **MVVM (Model-View-ViewModel)** architecture:

- **Data Layer**: Room Database (SQLite), Retrofit API service, Repository pattern
- **ViewModel Layer**: ViewModels for each feature
- **UI Layer**: Activities and Fragments

## Setup Instructions

### 1. Get TMDB API Key

1. Visit [TMDB API](https://www.themoviedb.org/settings/api)
2. Create a free account (if you don't have one)
3. Request an API key
4. Copy your API key

### 2. Configure API Key

1. Open `local.properties` file in the project root
2. Add your TMDB API key:
   ```
   TMDB_API_KEY=your_api_key_here
   ```
3. Save the file

**Note**: `local.properties` is already in `.gitignore`, so your API key won't be committed to version control.

### 3. Build and Run

1. Open the project in Android Studio
2. Sync Gradle files (File → Sync Project with Gradle Files)
3. Build the project (Build → Make Project)
4. Run on an emulator or physical device (minSdk 24)

## Project Structure

```
app/src/main/java/com/example/movieapp/
├── data/
│   ├── api/              # Retrofit API service
│   ├── local/            # Room database entities & DAOs
│   ├── models/           # POJO classes for API responses
│   └── repository/       # Data repository layer
├── ui/
│   ├── auth/             # Login/Register activities
│   ├── home/             # Home fragment with movie lists
│   ├── genres/           # Genres fragment
│   ├── search/           # Search fragment
│   ├── profile/          # Profile fragment
│   ├── details/          # Movie details activity
│   └── SplashActivity    # Splash screen
├── viewmodel/            # ViewModels for each feature
└── utils/                # Helper classes, constants
```

## Dependencies

- **Retrofit + Gson**: Networking and JSON parsing
- **Room Database**: SQLite wrapper for local data storage
- **ViewModel & LiveData**: MVVM architecture components
- **Glide**: Image loading
- **Material Design Components**: Modern UI components
- **RecyclerView & CardView**: List and card UI components

## Database Schema

### Users Table
- `id`: Primary key (auto-generated)
- `name`: User's name
- `email`: User's email (unique)
- `passwordHash`: Hashed password

### User Ratings Table
- `id`: Primary key (auto-generated)
- `userId`: Foreign key to users table
- `movieId`: TMDB movie ID
- `rating`: User rating (1.0 - 5.0)
- `timestamp`: Rating creation/update time

## API Endpoints Used

- `GET /movie/popular` - Popular movies
- `GET /movie/top_rated` - Top rated movies
- `GET /movie/upcoming` - Upcoming movies
- `GET /genre/movie/list` - Movie genres
- `GET /search/movie` - Search movies
- `GET /discover/movie` - Movies by genre
- `GET /movie/{id}` - Movie details

## Security Notes

- Passwords are hashed using SHA-256 (for production, consider using BCrypt or Argon2)
- API keys are stored in `local.properties` (not committed to version control)
- User sessions are managed via SharedPreferences

## Sample API Response

### Movie Response Example:
```json
{
  "page": 1,
  "results": [
    {
      "id": 550,
      "title": "Fight Club",
      "overview": "A ticking-time-bomb...",
      "poster_path": "/pB8BM7pdSp6B6Ih7QZ4DrQ3PmJK.jpg",
      "release_date": "1999-10-15",
      "vote_average": 8.4
    }
  ]
}
```

## Troubleshooting

### Build Errors
- Ensure you've added the TMDB API key to `local.properties`
- Sync Gradle files after adding dependencies
- Clean and rebuild the project (Build → Clean Project, then Build → Rebuild Project)

### Runtime Errors
- Check internet connection for API calls
- Verify API key is correct
- Check logcat for detailed error messages

## License

This project is for educational purposes.

