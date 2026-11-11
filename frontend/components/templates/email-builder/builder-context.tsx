"use client";

import {createContext, type DragEvent, useContext} from "react";
import type {BlockLocation} from "@/lib/utils/email-blocks";
import type {EmailBlock, EmailBlockType} from "@/types/domain/email-template";

/** dataTransfer types used for drag and drop inside the builder. */
export const DRAG_NEW_BLOCK = "application/x-email-block-type";
export const DRAG_MOVE_BLOCK = "application/x-email-block-id";

export interface BuilderActions {
  selectedId: string | null;
  select: (id: string | null) => void;
  update: (block: EmailBlock) => void;
  remove: (id: string) => void;
  duplicate: (id: string) => void;
  shift: (id: string, delta: -1 | 1) => void;
  dropNew: (type: EmailBlockType, location: BlockLocation) => void;
  dropMove: (id: string, location: BlockLocation) => void;
}

export const BuilderContext = createContext<BuilderActions | null>(null);

export function useBuilder(): BuilderActions {
  const context = useContext(BuilderContext);
  if (!context) throw new Error("useBuilder must be used inside EmailTemplateBuilder");
  return context;
}

/** True when the drag carries something the builder can drop. */
export function isBuilderDrag(event: DragEvent): boolean {
  const types = Array.from(event.dataTransfer.types);
  return types.includes(DRAG_NEW_BLOCK) || types.includes(DRAG_MOVE_BLOCK);
}

export function handleBuilderDrop(event: DragEvent, actions: BuilderActions, location: BlockLocation) {
  event.preventDefault();
  event.stopPropagation();
  const moveId = event.dataTransfer.getData(DRAG_MOVE_BLOCK);
  if (moveId) {
    actions.dropMove(moveId, location);
    return;
  }
  const type = event.dataTransfer.getData(DRAG_NEW_BLOCK) as EmailBlockType;
  if (type) actions.dropNew(type, location);
}
