# 🏟️ BattleArena API

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=mayademcom_battlearena-api&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=mayademcom_battlearena-api)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=mayademcom_battlearena-api&metric=coverage)](https://sonarcloud.io/summary/new_code?id=mayademcom_battlearena-api)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=mayademcom_battlearena-api&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=mayademcom_battlearena-api)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=mayademcom_battlearena-api&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=mayademcom_battlearena-api)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=mayademcom_battlearena-api&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=mayademcom_battlearena-api)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=mayademcom_battlearena-api&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=mayademcom_battlearena-api)

**A competitive gaming platform backend built with Spring Boot**

BattleArena is a multiplayer gaming backend system where warriors can battle, build alliances, and climb the leaderboards. This project serves as a comprehensive learning platform for backend development, database design, and modern software engineering practices.

## 🎯 Project Overview

### What We're Building

- **Warrior Management**: Player registration, authentication, and profiles
- **Battle System**: Real-time competitive matches with ranking
- **Alliance Network**: Social features for building warrior connections
- **Arena Leaderboards**: Global and friend-based rankings
- **Battle Analytics**: Performance tracking and statistics

### Learning Objectives

- **Spring Boot Development**: REST API design and implementation
- **Database Mastery**: PostgreSQL, complex queries, views, and functions
- **Modern Patterns**: Service layers, DTOs, and clean architecture
- **Collaboration**: Git workflows, code reviews, and agile practices
- **Professional Skills**: English technical communication and documentation

## 🛠️ Technology Stack

- **Backend**: Spring Boot 3.x, Java 17
- **Database**: PostgreSQL 15
- **Build Tool**: Maven
- **Containerization**: Docker & Docker Compose
- **Code Quality**: SonarCloud
- **CI/CD**: GitHub Actions
- **Version Control**: Git with Conventional Commits
- **Project Management**: Jira

## 🚀 Quick Start

### Prerequisites

- Java 17 or higher
- Maven 3.6+
- Docker and Docker Compose
- Git
- Your favorite IDE (IntelliJ IDEA recommended)

### 1. Clone the Repository

```bash
git clone <repository-url>
cd battlearena-api
```

### 2. Start the Database

```bash
# Start PostgreSQL in Docker
docker-compose up -d

# Verify database is running
docker-compose logs db
```

### 3. Run the Application

```bash
# Using Maven
./mvnw spring-boot:run

# Or using your IDE
# Run BattleArenaApplication.java
```

### 4. Verify Setup

```bash
# Check application status
curl http://localhost:8080/api/health

# Check database connection
curl http://localhost:8080/api/warriors/search?username=test
```

## 📊 Database Schema

Our database includes these main entities:

- **warriors**: Player accounts and statistics
- **battle_rooms**: Battle session management
- **battle_participants**: Who fought in which battles
- **warrior_alliances**: Friend connections and requests
- **battle_challenges**: Private warrior-vs-warrior matches
- **notifications**: System alerts and messages

### Advanced Database Features

- **Views**: `battle_results`, `warrior_statistics`, `arena_leaderboard`
- **Functions**: ELO rating calculations, statistics computations
- **Triggers**: Automatic warrior stats updates
- **Stored Procedures**: Complex battle completion workflows

## 🔧 Development Workflow

### Daily Routine

```bash
# Morning sync
git pull origin main
docker-compose up -d

# Start coding
./mvnw spring-boot:run

# Evening cleanup (optional)
docker-compose down
```

### Database Updates

When someone adds new database changes:

```bash
# Get latest schema changes
git pull origin main

# If major schema changes, refresh database
docker-compose down
rm -rf database/db_data
docker-compose up -d
```

## 🔧 Development Workflow

### Daily Routine

```bash
# Morning sync
git pull origin main
docker-compose up -d

# Start coding
./mvnw spring-boot:run

# Evening cleanup (optional)
docker-compose down
```

### Database Updates

When someone adds new database changes:

```bash
# Get latest schema changes
git pull origin main

# If major schema changes, refresh database
docker-compose down
rm -rf database/db_data
docker-compose up -d
```

### Git Workflow & Conventional Commits

We use [Conventional Commits](https://www.conventionalcommits.org/) for clear and standardized commit messages.

#### Commit Message Format

```
<type>[optional scope]: <description>

[optional body]

[optional footer(s)]
```

#### Types

- **feat**: A new feature for the user
- **fix**: A bug fix
- **docs**: Documentation only changes
- **style**: Changes that do not affect meaning (formatting, etc.)
- **refactor**: Code change that neither fixes a bug nor adds a feature
- **test**: Adding missing tests or correcting existing tests
- **chore**: Changes to build process or auxiliary tools

#### Examples

```bash
# New feature
git commit -m "feat(auth): add JWT token validation"

# Bug fix
git commit -m "fix(battle): resolve score calculation error"

# Database changes
git commit -m "feat(db): add warrior_alliances table"

# Documentation
git commit -m "docs(api): update battle endpoints documentation"

# Breaking change
git commit -m "feat(auth)!: replace basic auth with JWT"
```

#### Scopes (Optional)

- `auth`: Authentication related
- `battle`: Battle system features
- `alliance`: Social features
- `db`: Database changes
- `api`: API endpoints
- `config`: Configuration changes

### Creating New Features

1. Create feature branch: `git checkout -b feat/warrior-achievements`
2. Implement changes (database + code)
3. Use conventional commits for all changes
4. Ensure SonarQube quality gates pass
5. Test locally with Postman
6. Create pull request for code review
7. Merge after approval and quality checks

## 📁 Project Structure

```
battlearena-api/
├── src/main/java/
│   └── com/mayadem/battlearena/
│       ├── BattleArenaApplication.java
│       ├── config/          # Configuration classes
│       ├── controller/      # REST API endpoints
│       ├── dto/            # Data Transfer Objects
│       ├── entity/         # JPA entities
│       ├── repository/     # Data access layer
│       ├── service/        # Business logic
│       └── exception/      # Error handling
├── src/main/resources/
│   ├── application.properties
│   └── static/
├── database/
│   ├── init/              # Database schema files
│   │   ├── 01_warriors.sql
│   │   ├── 02_battle_rooms.sql
│   │   └── ...
│   └── sample-data/       # Test data
├── docker-compose.yml
├── pom.xml
└── README.md
```

## 🌟 Core Features

- **Warrior Management**: Player registration, authentication, and profiles
- **Battle System**: Competitive matches with ELO-based ranking
- **Alliance Network**: Social features for warrior connections
- **Leaderboards**: Global and friend-based rankings
- **Analytics**: Battle reports and performance tracking

## 🔌 API Endpoints

### Warrior Management

```
POST   /api/warriors/register     # Create new warrior account
POST   /api/warriors/login        # Authenticate warrior
GET    /api/warriors/profile      # Get current warrior profile
PUT    /api/warriors/profile      # Update profile information
PUT    /api/warriors/password     # Change password
```

### Battle System

```
POST   /api/battles               # Start new battle
POST   /api/battles/{id}/result   # Submit battle score
GET    /api/battles/history       # Get battle history
GET    /api/battles/leaderboard   # Global rankings
GET    /api/warriors/statistics   # Personal stats
```

### Social Features

```
GET    /api/warriors/search       # Find other warriors
POST   /api/alliances/request     # Send friend request
GET    /api/alliances             # Get friend list
PUT    /api/alliances/{id}/accept # Accept friend request
DELETE /api/alliances/{id}        # Remove alliance
```

## 📚 Learning Resources

### Spring Boot

- [Spring Boot Official Docs](https://spring.io/projects/spring-boot)
- [Building REST APIs with Spring Boot](https://spring.io/guides/tutorials/rest/)
- [Spring Data JPA Reference](https://spring.io/projects/spring-data-jpa)

### Database

- [PostgreSQL Tutorial](https://www.postgresql.org/docs/15/tutorial.html)
- [Database Design Principles](https://www.postgresql.org/docs/15/ddl.html)
- [SQL Performance Tips](https://www.postgresql.org/docs/15/performance-tips.html)

### Tools

- [Docker Compose Documentation](https://docs.docker.com/compose/)
- [Postman API Testing](https://learning.postman.com/docs/getting-started/introduction/)
- [Git Workflow Guide](https://www.atlassian.com/git/tutorials/comparing-workflows)

## 🔍 Code Quality & SonarCloud

### SonarCloud Integration

We use SonarCloud for continuous code quality inspection. All pull requests are automatically analyzed and must pass quality gates before merging.

#### Quality Gates

- **Coverage**: Minimum 80% test coverage
- **Duplications**: Less than 3% duplicated code
- **Maintainability**: Rating A
- **Reliability**: Rating A
- **Security**: Rating A
- **Security Hotspots**: All reviewed

#### GitHub Actions Workflow

Our CI/CD pipeline automatically:

- Runs unit tests on every push
- Performs SonarCloud analysis on PRs
- Prevents merging if quality gates fail
- Generates coverage reports
- Updates quality badges

#### Local SonarCloud Analysis (Optional)

```bash
# Run analysis locally (requires SONAR_TOKEN)
./mvnw clean verify sonar:sonar \
  -Dsonar.projectKey=mayademcom_battlearena-api \
  -Dsonar.organization=your-org \
  -Dsonar.host.url=https://sonarcloud.io \
  -Dsonar.login=$SONAR_TOKEN
```

#### Quality Guidelines

- Write meaningful unit tests for service layers
- Keep methods under 20 lines when possible
- Avoid code duplication - use helper methods
- Handle exceptions properly with custom exception classes
- Use meaningful variable and method names
- Add JavaDoc comments for public APIs

#### SonarCloud Dashboard

View detailed quality reports: [SonarCloud Project Dashboard](https://sonarcloud.io/project/overview?id=mayademcom_battlearena-api)

## 🚨 Troubleshooting

### Common Issues

**Database Connection Failed**

```bash
# Check if database is running
docker-compose ps

# Restart database
docker-compose restart db

# Check logs
docker-compose logs db
```

**Application Won't Start**

```bash
# Check Java version
java -version

# Clean and rebuild
./mvnw clean install

# Check application logs
./mvnw spring-boot:run --debug
```

**Port Already in Use**

```bash
# Check what's using port 8080
lsof -i :8080

# Kill the process or change port in application.properties
server.port=8081
```

## 🤝 Contributing

## 🤝 Contributing

## 🤝 Contributing

### Code Review Guidelines

- All code must be reviewed by at least one team member
- All PRs must pass SonarCloud quality gates
- GitHub Actions must pass (tests + quality checks)
- Use conventional commit format for all commits
- Test your changes locally before creating PR
- Include database migrations if schema changes

### Coding Standards

- Follow SonarCloud quality guidelines (see Code Quality section)
- Use meaningful variable and method names
- Add comments for complex business logic
- Follow Spring Boot naming conventions
- Keep methods focused and small (max 20 lines)
- Write unit tests for service layer methods

### Pull Request Process

```bash
# Feature development with conventional commits
git checkout -b feat/alliance-system
git add .
git commit -m "feat(alliance): add alliance request functionality"
git commit -m "test(alliance): add unit tests for alliance service"
git push origin feat/alliance-system

# Create Pull Request in GitHub
# ✅ GitHub Actions (CI) must pass
# ✅ SonarCloud quality gates must pass
# ✅ Code review approval required
# ✅ All tests must pass
# After approval, merge to main
```

### Git Workflow Best Practices

- Use descriptive branch names: `feat/warrior-stats`, `fix/login-bug`
- Keep commits small and focused
- Write clear commit messages using conventional format
- Rebase feature branches before merging
- Delete feature branches after merging

## 📞 Support

- **Technical Questions**: Ask during daily standups or in team chat
- **Database Issues**: Check docker-compose logs and PostgreSQL documentation
- **API Testing**: Use Postman collection and endpoint documentation
- **Git Problems**: Review Git workflow guide or ask team lead

## 🎮 Game On!

Ready to build the ultimate battle arena? Let's create something amazing together!

---

**Project Team**: Backend Engineering Interns  
**Duration**: 8 weeks  
**Goal**: Build production-ready gaming platform backend

_May the best warrior win! ⚔️_
