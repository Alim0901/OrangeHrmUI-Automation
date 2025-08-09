#FROM node:18
#WORKDIR /app
#COPY package*.json ./
#RUN ls -la /app && npm install
#COPY . .
#EXPOSE 7002
#CMD ["npm", "start"]

# Use OpenJDK 24.0.1 as base image
# Use OpenJDK 24.0.1 as base image
# ✅ Use Debian-based JDK image (supports apt-get)
FROM openjdk:21-jdk-bullseye

# 🛠️ Install system dependencies for Playwright and Java tooling
RUN apt-get update && \
    apt-get install -y --no-install-recommends \
    curl unzip zip gnupg2 git wget software-properties-common \
    ca-certificates bash libnss3 libatk1.0-0 libatk-bridge2.0-0 \
    libcups2 libdrm2 libxcomposite1 libxdamage1 libxrandr2 libgbm1 \
    libasound2 libxshmfence1 libxss1 libgtk-3-0 libx11-xcb1 && \
    apt-get clean && rm -rf /var/lib/apt/lists/*

# ✅ Install SDKMAN and Maven 3.9.9
RUN curl -s "https://get.sdkman.io" | bash && \
    bash -c "source /root/.sdkman/bin/sdkman-init.sh && sdk install maven 3.9.9"

# Set Maven environment variables
ENV SDKMAN_DIR="/root/.sdkman"
ENV PATH="${SDKMAN_DIR}/candidates/maven/current/bin:$PATH"

# 📌 Verify Maven
RUN bash -c "source /root/.sdkman/bin/sdkman-init.sh && mvn -version"

# 🔒 Update root certs
RUN update-ca-certificates

# ✅ Install Node.js (LTS) and npm
RUN curl -fsSL https://deb.nodesource.com/setup_lts.x | bash - && \
    apt-get install -y nodejs && \
    node -v && npm -v

# ✅ Install Playwright dependencies (browser libs)
RUN npx playwright install-deps

# 🔧 Optionally install Playwright browsers (if needed at build time)
# RUN npx playwright install

# ✅ Test network (will fail build if no internet)
RUN curl -I https://example.com

# 📁 Set working directory
WORKDIR /app

# 📦 Copy all source files into container
COPY . .

# 🚀 Default command (run tests)
CMD ["mvn", "clean", "verify"]

