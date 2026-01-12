# Quick Railway Setup Guide

## What You Need to Deploy on Railway

### 1. **Files Already Created for You:**
- ✅ `Dockerfile` - Container configuration
- ✅ `railway.toml` - Railway-specific settings
- ✅ `application-railway.properties` - Railway environment config

### 2. **Railway Account Setup:**
1. Go to [railway.app](https://railway.app)
2. Click "Login with GitHub"
3. Authorize Railway to access your repositories

### 3. **Deploy in 3 Steps:**

#### Step 1: Create Database
- Click "New Project" → "Provision PostgreSQL"
- Railway creates a PostgreSQL database automatically

#### Step 2: Deploy Your App
- In the same project, click "New Service" → "GitHub Repo"
- Select your `task-tracker-backend` repository
- Railway detects Dockerfile and starts building

#### Step 3: Set Environment Variables
In your app service settings, add:
```
SPRING_PROFILES_ACTIVE=railway
JWT_SECRET=mySecretKeyForJWTTokenGenerationThatShouldBeAtLeast256BitsLong
```

### 4. **That's It!**
- Railway automatically connects your app to the database
- Your API will be available at: `https://your-app-name.railway.app`
- Check deployment logs in the Railway dashboard

### 5. **Update Your Frontend**
In your GitHub Pages frontend, update the API URL:
```javascript
const API_BASE_URL = 'https://your-app-name.railway.app';
```

### 6. **Test Your Deployment**
- Visit: `https://your-app-name.railway.app/swagger-ui.html`
- Try the health check: `https://your-app-name.railway.app/actuator/health`

## Railway Benefits:
- ✅ Free $5/month credit (enough for small apps)
- ✅ Automatic HTTPS/SSL certificates
- ✅ Auto-scaling
- ✅ Built-in PostgreSQL
- ✅ GitHub integration
- ✅ Zero configuration needed

## Need Help?
- Railway docs: [docs.railway.app](https://docs.railway.app)
- Check deployment logs in Railway dashboard
- Ensure your GitHub repo is public or Railway has access