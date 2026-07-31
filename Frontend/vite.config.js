import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],

  server: {
    port: 3000,
    host: true,
    open: false,
    proxy: {
      // All /api/** calls → Spring Cloud Gateway
      '/api': {
        target: 'http://localhost:8443',
        changeOrigin: true,
        secure: false,
        configure: (proxy) => {
          proxy.on('error', () => {
            // Silently fail — offline mock mode takes over in service layer
          });
        }
      },
    },
  },

  build: {
    outDir: 'dist',
    sourcemap: false,
    // Split vendor chunks for better caching
    rollupOptions: {
      output: {
        manualChunks: {
          vendor: ['react', 'react-dom', 'react-router-dom'],
          axios: ['axios'],
          icons: ['lucide-react'],
        },
      },
    },
  },

  // Path aliases
  resolve: {
    alias: {
      '@': '/src',
      '@services': '/src/services',
      '@components': '/src/components',
      '@pages': '/src/pages',
      '@context': '/src/context',
    },
  },
});
