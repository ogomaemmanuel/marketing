import {SignalIcon, SmartphoneIcon, WifiIcon} from "lucide-react";
import {initials} from "@/lib/utils/format";

interface PhonePreviewProps {
  sender: string;
  message: string;
  segments: number;
}

function PhonePreview({ sender, message, segments }: PhonePreviewProps) {
  return (
    <div className="mx-auto w-full max-w-[19rem] rounded-[2.75rem] border-[10px] border-neutral-900 bg-neutral-900 shadow-xl">
      <div className="flex h-[34rem] flex-col overflow-hidden rounded-[2.1rem] bg-muted/40">
        <div className="relative flex items-center justify-between px-6 pt-3 pb-1 text-[11px] font-semibold text-foreground">
          <span>9:41</span>
          <span aria-hidden="true" className="absolute left-1/2 top-0 h-5 w-24 -translate-x-1/2 rounded-b-2xl bg-neutral-900" />
          <span className="flex items-center gap-1">
            <SignalIcon className="size-3" />
            <WifiIcon className="size-3" />
          </span>
        </div>

        <div className="flex flex-col items-center gap-1 border-b border-border/70 bg-card/70 px-4 pt-3 pb-3">
          <span className="flex size-10 items-center justify-center rounded-full bg-primary text-xs font-semibold text-primary-foreground">
            {initials(sender)}
          </span>
          <span className="text-xs font-medium text-foreground">{sender || "Sender"}</span>
          <span className="text-[10px] text-muted-foreground">SMS · text message</span>
        </div>

        <div className="flex-1 overflow-y-auto px-3 py-4">
          {message.trim() ? (
            <div className="max-w-[85%] whitespace-pre-wrap break-words rounded-2xl rounded-bl-md bg-card px-3.5 py-2 text-[13px] leading-snug text-foreground shadow-xs">
              {message}
            </div>
          ) : (
            <div className="flex h-full flex-col items-center justify-center gap-2 text-center text-xs text-muted-foreground">
              <SmartphoneIcon className="size-6" />
              Your SMS preview will appear here
            </div>
          )}
          {segments > 1 && (
            <p className="mt-1.5 pl-1 text-[10px] text-muted-foreground">Delivered as {segments} parts</p>
          )}
        </div>

        <div className="flex justify-center pb-2">
          <span className="h-1 w-24 rounded-full bg-neutral-900/70" />
        </div>
      </div>
    </div>
  );
}

export { PhonePreview };
