# Project Build Guide

This is simple application for getting information about nearest earth objects. This app integrate with Nasa API to get the informations

## Prerequisites

Before building the project, ensure you have the following installed:

1. **Java Development Kit (JDK)**
    - Version: 8 or higher (depending on your project's requirements)
    - Verify installation:
      ```bash
      java -version
      ```

2. **Apache Maven**
    - Version: 3.6.0 or higher
    - Verify installation:
      ```bash
      mvn -version
      ```

3. **Git** (optional, for fetching the repository)
    - Verify installation:
      ```bash
      git --version
      ```

## Clone the Repository

If you haven’t already cloned the project, use the following command:

```bash
git clone <repository-url>
cd <project-directory>
```

## Build the Project

To build the project, follow these steps:

1. Open a terminal/command prompt.
2. Navigate to the project root directory (where the `pom.xml` file is located).
3. Run the Maven build command:

   ```bash
   mvn clean package
   ```

## Build Output

After the build completes successfully, you can find the packaged application in the `target` directory:

- For a JAR project: `target/AsteroidsInfo-0.0.1-SNAPSHOT.jar`

## Run the Application

```bash
java -jar target/AsteroidsInfo-0.0.1-SNAPSHOT.jar
```
## API Endpoints

Below is a list of API endpoints available in this project:

### Example Endpoints

#### 1. **Get All Nearest Earth Objects (NEO)**
- **URL**: `/api/asteroids`
- **Method**: `GET`
- **Description**: Retrieves a list of all items.
- **Request Parameters**:
   - `startDate` (query): start date in yyyy-MM-dd. This is first date of the date range. This is optional
   - `endDate` (query): end date in yyyy-MM-dd. This is last date of the date range. This is optional
   - `distance` (query): distance should be in kilometers. This is optional. When the value provided, application will only include neo with distance lower than equals the parameter.
- **Response**:
  ```json
  {
    "status": "Success",
    "message": null,
    "data": {
        "2024-12-18": [
            {
                "id": "54503201",
                "neoReferenceId": "54503201",
                "name": "(2024 XS3)",
                "nameLimited": null,
                "designation": null,
                "absoluteMagnitudeH": 25.6,
                "links": {
                    "next": null,
                    "self": "http://api.nasa.gov/neo/rest/v1/neo/54503201?api_key=gHc1h6CQzsLi92mLWJAsOB53nGtLhvQjoSDWphJ6"
                },
                "estimatedDiameter": {
                    "kilometers": {
                        "estimatedDiameterMin": 0.0,
                        "estimatedDiameterMax": 0.0
                    },
                    "meters": {
                        "estimatedDiameterMin": 0.0,
                        "estimatedDiameterMax": 0.0
                    },
                    "miles": {
                        "estimatedDiameterMin": 0.0,
                        "estimatedDiameterMax": 0.0
                    },
                    "feet": {
                        "estimatedDiameterMin": 0.0,
                        "estimatedDiameterMax": 0.0
                    }
                },
                "closeApproachData": [
                    {
                        "closeApproachDate": "2024-12-18",
                        "closeApproachDateFull": "2024-Dec-18 01:54",
                        "epochDateCloseApproach": 1734486840000,
                        "missDistance": {
                            "astronomical": "0.0223824619",
                            "lunar": "8.7067776791",
                            "kilometers": "3348368.625596153",
                            "miles": "2080579.7861787914"
                        }
                    }
                ],
                "potentiallyHazardousAsteroid": false
            }
        ]
    }
  }
  ```
- **Response if start date and end date not provided**:
  ```json
  {
    "status": "Success",
    "message": null,
    "data": [
        {
            "id": "2000433",
            "neoReferenceId": "2000433",
            "name": "433 Eros (A898 PA)",
            "nameLimited": "Eros",
            "designation": "433",
            "absoluteMagnitudeH": 10.41,
            "links": {
                "next": null,
                "self": "http://api.nasa.gov/neo/rest/v1/neo/2000433?api_key=gHc1h6CQzsLi92mLWJAsOB53nGtLhvQjoSDWphJ6"
            },
            "estimatedDiameter": {
                "kilometers": {
                    "estimatedDiameterMin": 0.0,
                    "estimatedDiameterMax": 0.0
                },
                "meters": {
                    "estimatedDiameterMin": 0.0,
                    "estimatedDiameterMax": 0.0
                },
                "miles": {
                    "estimatedDiameterMin": 0.0,
                    "estimatedDiameterMax": 0.0
                },
                "feet": {
                    "estimatedDiameterMin": 0.0,
                    "estimatedDiameterMax": 0.0
                }
            },
            "closeApproachData": [
                {
                    "closeApproachDate": "1900-12-27",
                    "closeApproachDateFull": "1900-Dec-27 01:30",
                    "epochDateCloseApproach": -2177879400000,
                    "missDistance": {
                        "astronomical": "0.3149291693",
                        "lunar": "122.5074468577",
                        "kilometers": "47112732.928149391",
                        "miles": "29274494.7651919558"
                    }
                },
                {
                    "closeApproachDate": "1907-11-05",
                    "closeApproachDateFull": "1907-Nov-05 03:31",
                    "epochDateCloseApproach": -1961526540000,
                    "missDistance": {
                        "astronomical": "0.4714855425",
                        "lunar": "183.4078760325",
                        "kilometers": "70533232.893794475",
                        "miles": "43827318.620434755"
                    }
                }
            ],
            "potentiallyHazardousAsteroid": false
        }
    ]
  }
  ```

#### 2. **Get Detail NEO**
- **URL**: `/api/asteroids/{spkID}`
- **Method**: `GET`
- **Description**: Retrieves a specific item by its ID.
- **Request Parameters**:
- `spkID` (path): you can get the SPK ID from id field in /asteroids response
- **Response**:
  ```json
  {
    "status": "Success",
    "message": null,
    "data": {
        "id": "3794987",
        "neoReferenceId": "3794987",
        "name": "(2017 YD2)",
        "nameLimited": null,
        "designation": "2017 YD2",
        "absoluteMagnitudeH": 28.7,
        "links": {
            "next": null,
            "self": "http://api.nasa.gov/neo/rest/v1/neo/3794987?api_key=gHc1h6CQzsLi92mLWJAsOB53nGtLhvQjoSDWphJ6"
        },
        "estimatedDiameter": {
            "kilometers": {
                "estimatedDiameterMin": 0.0,
                "estimatedDiameterMax": 0.0
            },
            "meters": {
                "estimatedDiameterMin": 0.0,
                "estimatedDiameterMax": 0.0
            },
            "miles": {
                "estimatedDiameterMin": 0.0,
                "estimatedDiameterMax": 0.0
            },
            "feet": {
                "estimatedDiameterMin": 0.0,
                "estimatedDiameterMax": 0.0
            }
        },
        "closeApproachData": [],
        "potentiallyHazardousAsteroid": false
    }
   }
  ```