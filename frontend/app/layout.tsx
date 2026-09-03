import type { Metadata } from "next";
import { IBM_Plex_Mono, Instrument_Serif, Plus_Jakarta_Sans } from "next/font/google";
import "./globals.css";
import { QueryProvider } from "@/providers/query-provider";
import { AuthProvider } from "@/providers/auth-provider";
import { ThemeProvider } from "@/providers/theme-provider";
import { PaletteProvider } from "@/providers/palette-provider";
import { Toaster } from "@/components/ui/sonner";
import { DEFAULT_PALETTE, PALETTE_STORAGE_KEY, STOREFRONT_PALETTES } from "@/lib/theme/palettes";

const plusJakarta = Plus_Jakarta_Sans({
  variable: "--font-plus-jakarta",
  subsets: ["latin"],
});

const instrumentSerif = Instrument_Serif({
  variable: "--font-instrument-serif",
  subsets: ["latin"],
  weight: "400",
  style: ["normal", "italic"],
});

const ibmPlexMono = IBM_Plex_Mono({
  variable: "--font-ibm-plex-mono",
  subsets: ["latin"],
  weight: ["400", "500"],
});

export const metadata: Metadata = {
  title: "Marketing Platform",
  description: "Manage campaigns, contacts, templates and audiences.",
};

export default function RootLayout({ children }: LayoutProps<"/">) {
  return (
    <html
      lang="en"
      data-palette={DEFAULT_PALETTE}
      className={`${plusJakarta.variable} ${instrumentSerif.variable} ${ibmPlexMono.variable} h-full antialiased`}
      suppressHydrationWarning
    >
      <head>
        <script
          dangerouslySetInnerHTML={{
            __html: `(function(){try{var allowed=${JSON.stringify(STOREFRONT_PALETTES.map((palette) => palette.id))};var stored=localStorage.getItem(${JSON.stringify(PALETTE_STORAGE_KEY)});document.documentElement.setAttribute("data-palette", allowed.indexOf(stored)!==-1?stored:${JSON.stringify(DEFAULT_PALETTE)});}catch(e){document.documentElement.setAttribute("data-palette",${JSON.stringify(DEFAULT_PALETTE)});}})();`,
          }}
        />
      </head>
      <body className="min-h-full font-sans">
        <ThemeProvider attribute="class" defaultTheme="light" enableSystem={false}>
          <PaletteProvider>
            <AuthProvider>
              <QueryProvider>
                {children}
                <Toaster />
              </QueryProvider>
            </AuthProvider>
          </PaletteProvider>
        </ThemeProvider>
      </body>
    </html>
  );
}
