package com.pms.management.system.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pms.management.system.model.Resident;
import com.pms.management.system.model.Room;
import com.pms.management.system.service.ResidentService;
import com.pms.management.system.service.RoomService;

@Controller
@RequestMapping("/residents")
public class ResidentController {
    
    private static final Logger logger = LoggerFactory.getLogger(ResidentController.class);
    
    @Autowired
    private ResidentService residentService;
    
    @Autowired
    private RoomService roomService;
    
    @GetMapping
    public String listResidents(Model model, @RequestParam(required = false) String search) {
        List<Resident> residents;
        
        if (search != null && !search.trim().isEmpty()) {
            // Search functionality
            logger.info("Searching for residents with term: {}", search);
            residents = residentService.searchResidents(search);
            logger.info("Found {} residents matching search term '{}'", residents.size(), search);
            // Add the search term back to the model to maintain it in the form
            model.addAttribute("search", search);
        } else {
            // Original functionality - get all residents
            residents = residentService.getAllResidents();
            logger.info("Retrieved {} residents for listing", residents.size());
        }
        
        model.addAttribute("residents", residents);
        return "residents/list";
    }
    
    @GetMapping("/new")
    public String showNewResidentForm(Model model) {
        // Add a new resident object to the model
        model.addAttribute("resident", new Resident());
        
        // Generate predefined unit numbers (10-30)
        List<String> unitNumbers = new ArrayList<>();
        for (int i = 10; i <= 30; i++) {
            unitNumbers.add(String.valueOf(i));
        }
        logger.info("Generated {} unit numbers", unitNumbers.size());
        model.addAttribute("unitNumbers", unitNumbers);
        
        // Initialize rooms if needed
        try {
            roomService.initializeRooms();
            
            // Get rooms from database
            List<Room> rooms = roomService.getAllRooms();
            logger.info("Retrieved {} rooms from database for form", rooms.size());
            model.addAttribute("rooms", rooms);
            
            if (rooms.isEmpty()) {
                logger.warn("No rooms found in database after initialization");
                model.addAttribute("errorMessage", "No rooms available. Please contact administrator.");
            }
        } catch (Exception e) {
            logger.error("Error initializing or retrieving rooms: {}", e.getMessage(), e);
            model.addAttribute("errorMessage", "Error loading rooms: " + e.getMessage());
        }
        
        return "residents/form";
    }
    
    @PostMapping("/save")
    public String saveResident(@ModelAttribute("resident") Resident resident, 
                              BindingResult result, 
                              @RequestParam(value = "room.id", required = false) Long roomId,
                              RedirectAttributes redirectAttributes,
                              Model model) {
        logger.info("Saving resident: {}, Room ID from form: {}", resident, roomId);
        
        if (result.hasErrors()) {
            logger.error("Validation errors: {}", result.getAllErrors());
            
            // Re-populate form data
            List<String> unitNumbers = new ArrayList<>();
            for (int i = 10; i <= 30; i++) {
                unitNumbers.add(String.valueOf(i));
            }
            model.addAttribute("unitNumbers", unitNumbers);
            
            List<Room> rooms = roomService.getAllRooms();
            model.addAttribute("rooms", rooms);
            
            return "residents/form";
        }
        
        try {
            // Use roomId from @RequestParam if available, otherwise use the one from resident object
            Long effectiveRoomId = roomId;
            if (effectiveRoomId == null && resident.getRoom() != null) {
                effectiveRoomId = resident.getRoom().getId();
            }
            
            if (effectiveRoomId == null) {
                logger.error("Room ID is null");
                redirectAttributes.addFlashAttribute("errorMessage", "Room selection is required");
                return "redirect:/residents/new";
            }
            
            // Get the room by ID
            Room room = roomService.getRoomById(effectiveRoomId);
            if (room == null) {
                logger.error("Room not found with ID: {}", effectiveRoomId);
                redirectAttributes.addFlashAttribute("errorMessage", "Selected room not found");
                return "redirect:/residents/new";
            }
            
            // Set the room for the resident
            resident.setRoom(room);
            logger.info("Set room: {} (ID: {}) for resident", room.getRoomNumber(), room.getId());
            
            // Call the appropriate service method based on whether this is a new or existing resident
            if (resident.getId() == null) {
                logger.info("Creating new resident");
                residentService.createResident(resident);
            } else {
                logger.info("Updating existing resident with ID: {}", resident.getId());
                residentService.updateResident(resident);
            }
            
            logger.info("Resident saved successfully: {}", resident);
            redirectAttributes.addFlashAttribute("successMessage", 
                "Resident " + resident.getFirstName() + " " + resident.getLastName() + " saved successfully");
        } catch (Exception e) {
            logger.error("Error saving resident: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("errorMessage", "Error saving resident: " + e.getMessage());
            return "redirect:/residents/new";
        }
        
        return "redirect:/residents";
    }
    
    @GetMapping("/{id}/edit")
    public String showEditResidentForm(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Resident resident = residentService.getResidentById(id);
            if (resident == null) {
                logger.warn("Resident not found with ID: {}", id);
                redirectAttributes.addFlashAttribute("errorMessage", "Resident not found");
                return "redirect:/residents";
            }
            
            logger.info("Editing resident: {}", resident);
            model.addAttribute("resident", resident);
            
            // Generate predefined unit numbers (10-30)
            List<String> unitNumbers = new ArrayList<>();
            for (int i = 10; i <= 30; i++) {
                unitNumbers.add(String.valueOf(i));
            }
            model.addAttribute("unitNumbers", unitNumbers);
            
            // Get rooms from database
            List<Room> rooms = roomService.getAllRooms();
            logger.info("Retrieved {} rooms for edit form", rooms.size());
            model.addAttribute("rooms", rooms);
            
            if (rooms.isEmpty()) {
                logger.warn("No rooms found in database for edit form");
                model.addAttribute("errorMessage", "No rooms available. Please contact administrator.");
            }
            
            return "residents/form";
        } catch (Exception e) {
            logger.error("Error preparing edit form for resident ID {}: {}", id, e.getMessage(), e);
            redirectAttributes.addFlashAttribute("errorMessage", "Error loading resident: " + e.getMessage());
            return "redirect:/residents";
        }
    }
    
    @GetMapping("/{id}/delete")
    public String deleteResident(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            Resident resident = residentService.getResidentById(id);
            if (resident == null) {
                logger.warn("Cannot delete: Resident not found with ID: {}", id);
                redirectAttributes.addFlashAttribute("errorMessage", "Resident not found");
                return "redirect:/residents";
            }
            
            logger.info("Deleting resident: {}", resident);
            residentService.deleteResident(id);
            redirectAttributes.addFlashAttribute("successMessage", 
                "Resident " + resident.getFirstName() + " " + resident.getLastName() + " deleted successfully");
        } catch (Exception e) {
            logger.error("Error deleting resident with ID {}: {}", id, e.getMessage(), e);
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting resident: " + e.getMessage());
        }
        
        return "redirect:/residents";
    }
    
    @GetMapping("/init-rooms")
    public String initializeRooms(RedirectAttributes redirectAttributes) {
        try {
            logger.info("Manually initializing rooms");
            roomService.initializeRooms();
            List<Room> rooms = roomService.getAllRooms();
            logger.info("Rooms initialized successfully. Total rooms: {}", rooms.size());
            redirectAttributes.addFlashAttribute("successMessage", 
                "Rooms initialized successfully. Total rooms: " + rooms.size());
        } catch (Exception e) {
            logger.error("Error initializing rooms: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("errorMessage", "Error initializing rooms: " + e.getMessage());
        }
        return "redirect:/residents";
    }
}