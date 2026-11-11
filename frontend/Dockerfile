ARG NODE_BASE_IMAGE=node:24.22.2-alpine3.23
FROM --platform=$BUILDPLATFORM ${NODE_BASE_IMAGE} AS builder

ENV NODE_TLS_REJECT_UNAUTHORIZED=0
WORKDIR /app
RUN npm config set strict-ssl=false
# Copy package.json and package-lock.json first for better caching
COPY package.json package-lock.json ./
# Install dependencies
RUN npm ci

# Copy the rest of the application
COPY . .

# Build the Next.js application in standalone mode
RUN npm run build:standalone

# ----------- Production Image ------------
FROM ${NODE_BASE_IMAGE} AS runner

WORKDIR /app

ENV NODE_ENV=production
ENV PORT=3000
ENV HOSTNAME="0.0.0.0"
# Copy the built application from the builder stage
COPY --from=builder --chown=node:nodejs /app/.next/standalone ./

USER node
# Expose the port Next.js runs on
EXPOSE 3000

# Run the Next.js app
CMD ["node", "server.js"]
