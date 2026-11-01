// tailwind-glass.config.js - Tailwind configuration preset for premium liquid glass UI
module.exports = {
  theme: {
    extend: {
      colors: {
        ink: {
          950: '#0A0E1A',
          900: '#0F1526',
          850: '#131A2B',
          800: '#161D30',
        },
        glass: {
          dark: {
            thin: 'rgba(15, 21, 38, 0.55)',
            regular: 'rgba(15, 21, 38, 0.66)',
            thick: 'rgba(15, 21, 38, 0.78)',
            fallback: '#161D30',
          },
          light: {
            thin: 'rgba(255, 255, 255, 0.55)',
            regular: 'rgba(255, 255, 255, 0.70)',
            thick: 'rgba(255, 255, 255, 0.82)',
            fallback: '#F1F3F9',
          }
        }
      },
      borderRadius: {
        'control': '16px',
        'card': '24px',
        'nav': '28px',
        'sheet': '32px',
      },
      boxShadow: {
        'glass': '0 10px 28px -4px rgba(0, 0, 0, 0.18)',
        'glass-dark': '0 14px 32px -4px rgba(0, 0, 0, 0.38)',
      }
    }
  }
};

