# Web (CSS & Tailwind CSS) Implementation Guide

Modern web implementation for Apple-inspired liquid glass interfaces using pure CSS and Tailwind CSS.

## Architecture & Layering

```html
<!-- Content Layer (Natural document flow with opaque cards) -->
<main class="min-h-screen bg-[#F5F6FA] dark:bg-[#0A0E1A] text-slate-900 dark:text-white px-4 pb-28 pt-20">
  <div class="max-w-xl mx-auto space-y-4">
    <!-- OPAQUE Cards (Never glass) -->
    <article class="bg-white dark:bg-[#131A2B] rounded-3xl p-6 border border-black/5 dark:border-white/5 shadow-sm">
      <h2 class="text-xl font-semibold">Opaque Content Card</h2>
      <p class="text-slate-600 dark:text-slate-300 mt-2">Content remains clear and high-contrast.</p>
    </article>
  </div>
</main>

<!-- Floating Layer (Glass Controls) -->
<nav class="fixed bottom-6 left-1/2 -translate-x-1/2 z-50 glass-regular rounded-full px-6 py-3 flex items-center gap-6 shadow-2xl">
  <!-- Nav items -->
</nav>
```

## Material Tiers (CSS & Tailwind)

| Tier | Blur Radius | CSS Properties | Tailwind Classes |
|---|---|---|---|
| **Thin** | 16px | `-webkit-backdrop-filter: blur(16px); backdrop-filter: blur(16px);` | `backdrop-blur-md bg-white/55 dark:bg-[#0F1526]/55` |
| **Regular** | 24px | `-webkit-backdrop-filter: blur(24px); backdrop-filter: blur(24px);` | `backdrop-blur-xl bg-white/70 dark:bg-[#0F1526]/66` |
| **Thick** | 32px | `-webkit-backdrop-filter: blur(32px); backdrop-filter: blur(32px);` | `backdrop-blur-2xl bg-white/82 dark:bg-[#0F1526]/78` |

## Standard CSS Implementation (`glass.css`)

```css
/* Custom properties for glass design */
:root {
  --glass-border-light: rgba(255, 255, 255, 0.6);
  --glass-border-dark: rgba(255, 255, 255, 0.12);
  --glass-shadow: 0 10px 28px -4px rgba(0, 0, 0, 0.15);
}

/* Base Glass Utility */
.glass-surface {
  -webkit-backdrop-filter: blur(24px);
  backdrop-filter: blur(24px);
  background-color: rgba(255, 255, 255, 0.70);
  border: 1px solid var(--glass-border-light);
  box-shadow: var(--glass-shadow);
}

@media (prefers-color-scheme: dark) {
  .glass-surface {
    background-color: rgba(15, 21, 38, 0.66);
    border-color: var(--glass-border-dark);
    box-shadow: 0 14px 32px -4px rgba(0, 0, 0, 0.35);
  }
}

/* Accessibility: Reduced Transparency Support */
@media (prefers-reduced-transparency: reduce) {
  .glass-surface {
    -webkit-backdrop-filter: none;
    backdrop-filter: none;
    background-color: #F1F3F9;
    border: 1px solid rgba(0, 0, 0, 0.12);
  }

  @media (prefers-color-scheme: dark) {
    .glass-surface {
      background-color: #161D30;
      border: 1px solid rgba(255, 255, 255, 0.15);
    }
  }
}

/* Fallback for browsers without backdrop-filter support */
@supports not ((-webkit-backdrop-filter: blur(1px)) or (backdrop-filter: blur(1px))) {
  .glass-surface {
    background-color: #FFFFFF;
  }
  @media (prefers-color-scheme: dark) {
    .glass-surface {
      background-color: #161D30;
    }
  }
}
```

## Tailwind CSS Configuration (`tailwind.config.js`)

```javascript
/** @type {import('tailwindcss').Config} */
module.exports = {
  darkMode: 'class',
  theme: {
    extend: {
      colors: {
        ink: {
          950: '#0A0E1A',
          900: '#0F1526',
          800: '#131A2B',
          700: '#161D30',
        },
        accent: {
          indigo: '#5B6CFF',
          emerald: '#2BD48A',
          coral: '#FF5C6C',
        }
      },
      borderRadius: {
        'card': '24px',
        'nav': '28px',
        'sheet': '32px',
      },
      boxShadow: {
        'glass': '0 10px 28px -4px rgba(0, 0, 0, 0.18)',
        'glass-dark': '0 14px 32px -4px rgba(0, 0, 0, 0.38)',
      }
    },
  },
  plugins: [],
}
```

## Reusable Component Classes (Tailwind)

```html
<!-- Floating Nav Pill -->
<nav class="fixed bottom-6 left-1/2 -translate-x-1/2 z-50 h-16 px-6 
            backdrop-blur-xl bg-white/70 dark:bg-ink-900/66 
            border border-white/60 dark:border-white/10 
            rounded-nav shadow-glass dark:shadow-glass-dark 
            flex items-center gap-6">
  <button class="flex flex-col items-center text-accent-indigo text-xs font-semibold">
    <svg ... class="w-6 h-6"></svg>
    <span>Home</span>
  </button>
  <button class="flex flex-col items-center text-slate-500 dark:text-slate-400 hover:text-slate-900 text-xs">
    <svg ... class="w-6 h-6"></svg>
    <span>Explore</span>
  </button>
</nav>
```

## Web Performance & Best Practices
1. **Always include `-webkit-backdrop-filter`**: Safari requires the vendor prefix; omitting it causes glass elements to appear flat or translucent without any blur.
2. **Never place backdrop blur inside list items or tables**: Only use on fixed/sticky navigation, dialogs, drawers, and floating action buttons.
3. **Respect `prefers-reduced-transparency`**: Always supply an opaque surface alternative for users who have transparency disabled in their OS.
4. **Contrast check**: Ensure text meets WCAG AA (4.5:1 for body text, 3.0:1 for large text/icons) over both bright images and dark backdrops.

